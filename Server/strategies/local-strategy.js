var passport = require("passport");
var passportJWT = require("passport-jwt");
var cfg = require("../config.js");
var ExtractJwt = passportJWT.ExtractJwt;
var JwtStrategy = passportJWT.Strategy;
var UserModel = require("../data/models/userModel");

var params = {
    secretOrKey: cfg.jwtSecret,
    jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken()
};

var strategy = new JwtStrategy(params, function (jwt_payload, next) {
    UserModel.findOne({
        _id: jwt_payload.id
    }).lean().exec((error, user) => {
        if (user) {
            next(null, user);
        } else {
            next(null, false);
        }
    });
});

module.exports = strategy;