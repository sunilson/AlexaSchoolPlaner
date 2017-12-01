var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var eventVariables = require('../../variables/EventVariables');
var userVariables = require('../../variables/UserVariables');
var passwordHash = require("password-hash");
var validate = require('mongoose-validator');
var Event = require('../schemas/eventSchema');

var usernameValidator = [
    validate({
        validator: 'isAlphanumeric',
        message: 'Username contains invalid characters!'
    }),
    validate({
        validator: 'isLength',
        arguments: [userVariables.userName.minLength, userVariables.userName.maxLength],
        message: "Username is too long or too short!"
    })
];

var passwordValidator = [
    validate({
        validator: 'isLength',
        arguments: [userVariables.password.minLength, userVariables.password.maxLength],
        passIfEmpty: true,
        message: "Password is too long or too short!"
    })
];

var emailValidator = [
    validate({
        validator: 'isEmail',
        message: 'Email is not valid!'
    })
];

var userSchema = new Schema({
    username: {
        type: String,
        required: true,
        validate: usernameValidator,
        index: {
            unique: true
        }
    },
    email: {
        type: String,
        required: true,
        validate: emailValidator,
        index: {
            unique: true
        }
    },
    password: {
        type: String,
        validate: passwordValidator
    },
    activated: {
        type: Boolean
    },
    type: {
        type: String,
        required: true
    },
    created_at: Date
}, {
    collection: "users"
});

userSchema.pre('save', function (next) {
    //Set creation date
    if (!this.created_at) {
        this.created_at = new Date();
    }

    //Set activation status
    if (!this.activated) {
        if (this.type !== UserVariables.type.standard) {
            this.activated = true;
        } else {
            this.activated = false;
        }
    }

    //Transform username
    this.username = this.username.toLowerCase();

    if (!this.isModified('password')) return next();

    //Hash password
    if (this.password) {
        this.password = passwordHash.generate(this.password);
    }

    next();
});


module.exports = userSchema;