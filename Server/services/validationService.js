var randtoken = require("rand-token");
var ValidationTokenModel = require("../data/models/ValidationTokenModel");
var cfg = require("../config.js");
var mailgun = require('mailgun-js')({
    apiKey: cfg.mailGunKey,
    domain: cfg.mailGunDomain
});
var AccountActivationTemplate = require("../mail-templates/en/account-activation");

var tokenService = module.exports = {};

tokenService.sendVerification = function (id, email) {

    return new Promise((resolve, reject) => {
        var token = randtoken.generate(24);

        new ValidationTokenModel({
            userID: id,
            validationToken: token
        }).save((err, result) => {
            if (err) reject(err);

            //Send verification email !!!Dont use arrow function, it breaks the code!!!
            var template = new AccountActivationTemplate(email, token);
            mailgun.messages().send(template.message, function (error, body) {
                if (error) return reject(error);

                resolve();
            });
        });
    });
}