var mongoose = require("mongoose");
var refreshTokenSchema = require("../schemas/RefreshTokenSchema");

var refreshToken = mongoose.model("RefreshToken", refreshTokenSchema);

module.exports = refreshToken;