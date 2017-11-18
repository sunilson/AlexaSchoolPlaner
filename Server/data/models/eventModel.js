var mongoose = require('mongoose');
var eventSchema = require('../schemas/eventSchema');

module.exports = mongoose.model('Event', eventSchema);