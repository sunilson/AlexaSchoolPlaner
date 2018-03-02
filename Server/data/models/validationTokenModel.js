var mongoose = require("mongoose");
var ValidationTokenSchema = require("../schemas/validationTokenSchema");

var validationToken = mongoose.model("ValidationToken", ValidationTokenSchema);

module.exports = validationToken;