const UserModel = require("../data/models/userModel");
const express = require('express');
const router = express.Router();
const jwt = require("jsonwebtoken");
const cfg = require("../config.js");
const mongoose = require("mongoose");
const refreshTokenModel = require("../data/models/refreshTokenModel");
const passwordHash = require("password-hash");
const validator = require("validator");
const GoogleAuth = require('google-auth-library');
const auth = new GoogleAuth;
const client = new auth.OAuth2(cfg.googleAuthClientId, '', '');
const userService = require("../services/userService");
const tokenService = require("../services/tokenService");
const UserVariables = require("../variables/UserVariables");
const ObjectOperations = require("../utils/objectOperations");
const validationService = require("../services/validationService");
const randtoken = require("rand-token");
const dots = require("dot").process({
    path: "./views"
});
var ValidationTokenModel = require("../data/models/validationTokenModel")


//Renders the alexa login page
router.get('/alexaLoginPage', (req, res, next) => {
    res.send(dots.loginPage({
        state: req.query.state,
        redirect_uri: req.query.redirect_uri
    }));
});

//Processes the alexa login form
router.post('/alexaLoginPage', (req, res, next) => {

    const name = req.body.name
    const password = req.body.password
    const state = req.body.state
    const redirect_uri = req.body.redirect_uri

    UserModel.findOne((validator.isEmail(name)) ? {
        email: name
    } : {
        username: name
    }).lean().exec().then(result => {
        if (checkLoginData(result, password)) return validationService.generateAmazonCode(result._id)
        else throw new Error("Invalid data")
    }).then(amazonToken => {
        //Form data was correct, redirect to given URL
        res.writeHead(302, {
            'Location': `${redirect_uri}?state=${state}&code=${amazonToken}`
        });
        res.end();
    }).catch(e => {
        //Form data was invalid, render form again
        return res.status(401).send(dots.loginPage({
            state: state,
            redirect_uri: redirect_uri,
            error: "Invalid data!"
        }));
    })
});

//Route used for authentication with Access and Refresh tokens
router.post('/', (req, res, next) => {
    //Check if request comes from alexa service
    if (req.body.grant_type === "authorization_code" && req.body.code) {
        //TODO: Check if client id is correct and request is from Amazon
        //Check if given code is valid
        ValidationTokenModel.findOne({
            validationToken: req.body.code
        }).lean().exec().then(result => {
            //Generate access and refresh token
            return tokenService.generateTokens(result.userID, null)
        }).then(tokens => {
            //Return tokens to alexa service
            res.status(200).json(tokens)
        }).catch(e => next(e))
    } else if (req.body.grant_type === "refresh_token" || req.body.refresh_token) {
        //Get data from request
        const refreshToken = req.body.refresh_token;
        if (!refreshToken) {
            return res.sendStatus(400);
        }

        //Check if refresh token is valid
        tokenService.checkRefreshToken(refreshToken).then(userId => {
            return tokenService.generateTokens(userId, refreshToken)
        }).then(tokens => {
            res.send(tokens);
        }).catch(e => {
            next(e)
        })
    } else {
        let user = ObjectOperations.trimObject(req.body);
        //Compare request data with database and retrieve user object
        UserModel.findOne((validator.isEmail(user.name)) ? {
            email: user.name
        } : {
            username: user.name
        }).lean().exec((error, result) => {
            if (error) return next(error);
            checkLoginDataAndReturnUser(result, user.password, res, next);
        })
    }
});

/**
 * Route for registering a new user
 */
router.post('/register', function (req, res, next) {

    var parsedUser = ObjectOperations.trimObject(req.body);

    var results = {}
    //Validate and save data to database
    userService.saveUser(parsedUser).then((user) => {
        //User was succesfully inserted
        //Generate fresh tokens so user can login immediately
        results.user = user;
        return tokenService.generateTokens(user.id, null);
    }).then((tokens) => {
        //Send verification mail to new user
        results.tokens = tokens;
        //return validationService.sendVerification(results.user.id, results.user.email);
    }).then((validationResult) => {
        //Return login tokens to the requester
        return res.status(201).json({
            user: {
                id: results.user.id,
                username: results.user.username,
                email: results.user.email
            },
            tokens: result.tokens
        });
    }).catch(err => {
        //If user can't be saved, give error to error handler
        return next(err);
    });
});

//Verify user account with token
router.post('/verify', function (req, res, next) {

    if (!req.body.token) {
        //No token given
        return res.sendStatus(401);
    }

    var token = req.body.token;
    //Find verification Token
    ValidationTokenModel.findOne({
        validationToken: token
    }).lean().exec((error, result) => {
        if (error) return next(error);
        //If a token has been found
        if (!result) {
            //No token found
            return res.status(400).json({
                success: false,
                message: "Token not valid!"
            })
        }

        //Get userID from database entry and set user to activated
        var id = result.userID;
        UserModel.update({
            _id: id
        }, {
            activated: true
        }, null, (error) => {
            if (error) return next(error);
            //Remove verification token
            result.remove((error, result) => {
                if (error) return next(error);
                //Return success to client
                res.status(201).json({
                    success: true,
                    message: "Activation success!"
                })
            });
        });
    });
});


//Check if login data is correct and return access/refresh tokens and the user details
function checkLoginData(user, password) {
    if (user && user.type === UserVariables.type.standard && passwordHash.verify(password, user.password)) return true
    return false
}

//Checks login data, generates tokens for the user and returns the user 
function checkLoginDataAndReturnUser(user, password, res, next) {
    if (checkLoginData(user, password)) {
        //Generate fresh tokens
        tokenService.generateTokens(user._id, null).then((tokens) => {
            //Return the tokens to the requester
            const tempUser = {
                id: user._id,
                username: user.username,
                email: user.email
            }
            if (user.icalurl) tempUser["icalurl"] = user.icalurl
            res.json({
                tokens: tokens,
                user: tempUser
            });
        }).catch(error => next(error));
    } else res.sendStatus(401)
}

module.exports = router;