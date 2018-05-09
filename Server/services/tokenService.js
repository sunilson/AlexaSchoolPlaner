var jwt = require("jsonwebtoken");
var refreshTokenModel = require("../data/models/refreshTokenModel");
var cfg = require("../config.js");
var UserVariables = require("../variables/UserVariables");
var tokenService = module.exports = {};

//Generate a JWT Token and pass given id as payload
tokenService.generateToken = function (id) {
    //Payload of access token
    var payload = {
        id: id
    };

    //Generate new access token
    return jwt.sign(payload, cfg.jwtSecret, {
        expiresIn: UserVariables.auth.accessExpire
    });
}

//Check in database if a given refresh token is valid and not expired
tokenService.checkRefreshToken = async function (token) {
    const tokenResult = await refreshTokenModel.findOne({
        refreshToken: token
    }).lean().exec()

    //Check if token is valid and not expired
    if (!tokenResult) {
        const error = new Error("No token found")
        error.status = 400
        throw error
    }

    //Check if refresh token is valid and not expired
    const payload = jwt.verify(tokenResult.refreshToken, cfg.jwtRefreshSecret)
    return payload.id
}

/**
 * Creates a pair of access and refresh tokens from a user ID
 * 
 * @param id User id of author
 * @param refreshToken If an existing refreshToken should be used
 */
tokenService.generateTokens = async function (id, refreshToken) {

    if (!id) throw new Error("User ID was empty")

    const payload = {
        id: id
    };

    //Generate new access token
    const accessToken = jwt.sign(payload, cfg.jwtSecret, {
        expiresIn: UserVariables.auth.accessExpire
    });

    if (refreshToken == null) {
        //Generate new refresh token
        refreshToken = jwt.sign(payload, cfg.jwtRefreshSecret, {
            expiresIn: UserVariables.auth.refreshExpire
        });
        //Store refresh token in database with user id and expiration date
        await new refreshTokenModel({
            userID: id,
            refreshToken: refreshToken
        }).save()
    }

    return {
        access_token: accessToken,
        token_type: "bearer",
        expires_in: UserVariables.auth.accessExpire,
        refresh_token: refreshToken
    }
}