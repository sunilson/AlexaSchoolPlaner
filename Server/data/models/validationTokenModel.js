var mongoose = require("mongoose");
var ValidationTokenSchema = require("../schemas/ValidationTokenSchema");

var validationToken = mongoose.model("ValidationToken", ValidationTokenSchema);

module.exports = validationToken;