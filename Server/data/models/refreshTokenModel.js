var mongoose = require("mongoose");
var refreshTokenSchema = require("../schemas/refreshTokenSchema");

var refreshToken = mongoose.model("RefreshToken", refreshTokenSchema);

module.exports = refreshToken;