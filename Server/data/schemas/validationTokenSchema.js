var mongoose = require("mongoose");
var Schema = mongoose.Schema;

var validationTokenSchema = new Schema({
    userID: {
        type: Schema.Types.ObjectId,
        required: true
    },
    validationToken: {
        type: String,
        required: true,
        index: {
            unique: true
        }
    }
}, {
    collection: "validationTokens"
});

module.exports = validationTokenSchema;