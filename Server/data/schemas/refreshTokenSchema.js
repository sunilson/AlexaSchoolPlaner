var mongoose = require("mongoose");
var Schema = mongoose.Schema;

var refreshTokenSchema = new Schema({
    userID: {
        type: Schema.Types.ObjectId,
        required: true
    },
    refreshToken: {
        type: String,
        required: true,
        index: {
            unique: true
        }
    }
}, {
    collection: "refreshTokens"
});

module.exports = refreshTokenSchema;