var jwt = require("jsonwebtoken");
var randtoken = require("rand-token");
var refreshTokenModel = require("../data/models/RefreshTokenModel");
var cfg = require("../config.js");
var UserVariables = require("../variables/UserVariables");
var tokenService = module.exports = {};

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

tokenService.generateTokens = function (id) {

    return new Promise((resolve, reject) => {
        if (id) {
            //Payload of access token
            var payload = {
                id: id
            };

            //Generate new access token
            var accessToken = jwt.sign(payload, cfg.jwtSecret, {
                expiresIn: UserVariables.auth.accessExpire
            });

            //Generate new refresh token
            var refreshToken = jwt.sign(payload, cfg.jwtRefreshSecret, {
                expiresIn: UserVariables.auth.refreshExpire
            });

            //Store refresh token in database with user id and expiration date
            new refreshTokenModel({
                userID: id,
                refreshToken: refreshToken
            }).save((err, result) => {
                if (err) reject(err);
                resolve({
                    accessToken: accessToken,
                    refreshToken: refreshToken
                });
            });
        } else {
            reject({
                message: "User ID was empty!",
                status: 400
            });
        }
    });
}