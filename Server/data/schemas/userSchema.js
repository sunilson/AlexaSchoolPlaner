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

var nameValidator = [
    validate({
        validator: 'isAlpha',
        message: 'Name contains invalid characters!'
    }),
    validate({
        validator: 'isLength',
        arguments: [userVariables.name.minLength, userVariables.name.maxLength],
        message: "Name is too long or too short!"
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
    events: [Event],
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
    firstname: {
        type: String,
        validate: nameValidator
    },
    lastname: {
        type: String,
        validate: nameValidator
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

    //Hash password
    if (this.password) {
        this.password = passwordHash.generate(this.password);
    }

    //Transform username
    this.username = this.username.toLowerCase();

    //If proifle picture is empty, load default picture
    if (this.profilepicture) {
        this.profilepicture = "http://www.planystech.com/wp-content/uploads/2017/03/profile-placeholder.jpg";
    }

    next();
});


module.exports = userSchema;