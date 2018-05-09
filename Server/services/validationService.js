var randtoken = require("rand-token");
var ValidationTokenModel = require("../data/models/validationTokenModel");
var cfg = require("../config.js");
var mailgun = require('mailgun-js')({
    apiKey: cfg.mailGunKey,
    domain: cfg.mailGunDomain
});
var AccountActivationTemplate = require("../mail-templates/en/account-activation");

var tokenService = module.exports = {};

//Send a randomly generated token to an email to verify it
tokenService.sendVerification = function (id, email) {
    return new Promise((resolve, reject) => {
        const token = randtoken.generate(24);

        //Store token in database with user id for later verification
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

//Generate a random token to be used with an Amazon auth request
tokenService.generateAmazonCode = async function (id) {
    const token = randtoken.generate(24);
    //Save token in database like an email verify token
    await new ValidationTokenModel({
        userID: id,
        validationToken: token
    }).save()
    return token
}