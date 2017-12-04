var userService = module.exports = {};
var UserModel = require("../data/models/UserModel")
var UserVariables = require("../variables/UserVariables");
var randtoken = require("rand-token");
var ObjectOperations = require("../utils/objectOperations");

userService.saveUser = (user) => {
    return new Promise((resolve, reject) => {
        //Check type and if input needs to be validated
        if (!user.type || user.type === UserVariables.type.standard) {
            user.type = UserVariables.type.standard;
            if (!user.password) {
                reject({
                    message: "Password is required!",
                    status: 400
                });
            }
        } else if (user.type !== UserVariables.type.standard) {
            user.username = user.type.charAt(0) + "." + formatSocialUsername();
        }

        //Check if "activated" field is set. If yes, remove it to prevent the user to set this himself
        if (user.activated) {
            delete user.activated;
        }

        //Check if username and email are unique
        Promise.all([checkUniqueValue(user.email, "email"), checkUniqueValue(user.username, "username")]).then((result) => {
            //Create model and save it into database. Only save required attributes to prevent errors/manipulating
            new UserModel(user).save((err, result) => {
                if (err) reject(err);
                resolve(result);
            });
        }).catch((err) => {
            //Username or email are not unique
            reject(err);
        });
    });
}

//Remove spacing from social usernames
function formatSocialUsername() {
    return randtoken.generate(10);
}

//Check if a value already exists in the database
function checkUniqueValue(value, key) {
    var filter = {};
    filter[key] = value;
    return new Promise((resolve, reject) => {
        UserModel.findOne(filter).lean().exec((err, result) => {
            if (result) {
                reject({
                    status: 400,
                    message: "Username or Email already exists!"
                });
            } else {
                resolve();
            }
        });
    });
}