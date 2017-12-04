var express = require('express');
var router = express.Router();
var ObjectOperations = require("../utils/objectOperations");
var UserModel = require("../data/models/userModel");

var express = require('express');
var router = express.Router();
var jwt = require("jsonwebtoken");
var cfg = require("../config.js");
var mongoose = require("mongoose");
var tokenService = require("../services/tokenService");
var refreshTokenModel = require("../data/models/RefreshTokenModel");
var UserModel = require("../data/models/UserModel");
var passwordHash = require("password-hash");
var validator = require("validator");
var GoogleAuth = require('google-auth-library');
var auth = new GoogleAuth;
var client = new auth.OAuth2(cfg.googleAuthClientId, '', '');
var userService = require("../services/userService");
var tokenService = require("../services/tokenService");
var UserVariables = require("../variables/UserVariables");
var ObjectOperations = require("../utils/objectOperations");
var validationService = require("../services/validationService");

router.get('/refreshToken', function (req, res) {
    //Get data from request
    var refreshToken = req.query.refreshToken;
    if (!refreshToken) {
        return res.sendStatus(400);
    }

    //Get token from database
    refreshTokenModel.findOne({
        refreshToken: refreshToken
    }).lean().exec((err, token) => {
        if (err) throw err;

        //Check if token is valid and not expired
        if (!token) {
            return res.sendStatus(400);
        }

        //Check if refresh token is valid and not expired
        jwt.verify(token.refreshToken, cfg.jwtRefreshSecret, (error, decoded) => {
            if (error) {
                res.sendStatus(401);
            } else {
                var userId = decoded.id;
                var accessToken = tokenService.generateToken(userId);
                res.status(200).json(accessToken);
            }
        });
    });
});

//Login route
router.post('/login', function (req, res, next) {

    let user = ObjectOperations.trimObject(req.body);
    console.log(user);

    //Compare request data with database and retrieve user object
    if (validator.isEmail(name)) {
        UserModel.findOne({
            email: name
        }).lean().exec((error, result) => {
            if (error) return next(error);
            checkLoginData(result, password, res, next);
        });
    } else {
        UserModel.findOne({
            username: name
        }).lean().exec((error, result) => {
            if (error) return next(error);
            checkLoginData(result, password, res, next);
        });
    }
});

router.post('/register', function (req, res, next) {

    var parsedUser = ObjectOperations.trimObject(req.body);
    console.log(parsedUser);

    var results = {}
    //Validate and save data to database
    userService.saveUser(parsedUser).then((user) => {
        //User was succesfully inserted
        //Generate fresh tokens so user can login immediately
        results.user = user;
        return tokenService.generateTokens(user.id);
    }).then((tokens) => {
        //Send verification mail to new user
        results.tokens = tokens;
        //return validationService.sendVerification(results.user.id, results.user.email);
    }).then((validationResult) => {
        //Return login tokens to the requester
        console.log({
            user: {
                id: results.user.id,
                username: results.user.username,
                email: results.user.email
            },
            tokens: {
                accessToken: results.tokens.accessToken,
                refreshToken: results.tokens.refreshToken
            }
        });
        return res.status(201).json({
            user: {
                id: results.user.id,
                username: results.user.username,
                email: results.user.email
            },
            tokens: {
                accessToken: results.tokens.accessToken,
                refreshToken: results.tokens.refreshToken
            }
        });
    }).catch((err) => {
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
function checkLoginData(user, password, res, next) {
    if (user && user.type === UserVariables.type.standard && passwordHash.verify(password, user.password)) {
        //Generate fresh tokens
        tokenService.generateTokens(user._id).then((tokens) => {
            //Return the tokens to the requester
            res.json({
                tokens: {
                    accessToken: tokens.accessToken,
                    refreshToken: tokens.refreshToken
                },
                user: {
                    id: user._id,
                    username: user.username,
                    email: user.email
                }
            });
        }).catch((error) => {
            return next(error);
        });
    } else {
        res.sendStatus(400);
    }
}

module.exports = router;