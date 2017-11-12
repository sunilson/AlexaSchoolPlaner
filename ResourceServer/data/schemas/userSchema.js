var mongoose = require('mongoose');
var eventVariables = require('../variables/EventVariables');
var validate = require('mongoose-validator');
var Event = require('../schemas/eventSchema');

var userSchema = new mongoose.Schema({
    events: [Event]
}, options);

module.exports = userSchema;