var mongoose = require('mongoose');
var eventVariables = require('../variables/EventVariables');
var validate = require('mongoose-validator');
var isCoordinates = require('is-coordinates');

var descriptionValidator = [
    validate({
        validator: 'isLength',
        passIfEmpty: true,
        arguments: [eventVariables.description.minLength, eventVariables.description.maxLength],
        message: 'Summary length is invalid!'
    })
];

var dateValidator = [
    validate({

    })
];

var typeValidator = [
    validate({
        validator: (val) => {
            return Number.isInteger(val) && val <= eventVariables.types.deadline && val >= eventVariables.types.schoolAppointment;
        },
        message: 'Type is invalid!'
    })
];

var geoValidator = [
    validate({
        validator: (val) => {
            return isCoordinates(val);
        },
        passIfEmpty: true,
        message: "Coordinates are invalid!"
    })
];

var locationValidator = [
    validate({
        validator: 'isLength',
        arguments: [eventVariables.location.minLength, eventVariables.location.maxLength],
        message: 'Location length is invalid!'
    }),
    validate({
        validator: 'isAscii',
        arguments: [eventVariables.location.minLength, eventVariables.location.maxLength],
        message: 'Location contains invalid characters!'
    }),
];

var summaryValidator = [
    validate({
        validator: 'isLength',
        arguments: [eventVariables.summary.minLength, eventVariables.summary.maxLength],
        message: 'Title length is invalid!'
    })
];

var eventSchema = new mongoose.Schema({
    description: {
        required: true,
        type: String,
        validate: descriptionValidator
    },
    summary: {
        type: String,
        validate: summaryValidator
    },
    from: {
        required: true,
        type: Date
    },
    to: {
        required: true,
        type: Date
    },
    type: {
        type: Number,
        default: eventVariables.types.schoolAppointment,
        validate: typeValidator
    },
    location: {
        type: String,
        validate: locationValidator
    },
    geo: {
        type: Number[],
        index: "2dsphere",
        validate: geoValidator
    }
});

module.exports = eventSchema;