var eventService = module.exports = {};
var eventModel = require("../data/eventModel");
var eventVariables = require('../variables/EventVariables');

eventService.saveEvent = (event, userId) => {

    //Parse timestamps to Javascript Dates
    if (event.to && event.from && Number.isInteger(event.to) && Number.isInteger(event.from)) {
        event.from = new Date(event.from);

        //If event is a deadline, offset to date by 1 hour
        //Also if to Date is smaller than the from date offset it
        if ((event.type && event.type === eventVariables.types.deadline) || (event.from.getMilliseconds() > event.to)) {
            event.to = new Date(event.from.getMilliseconds() + eventVariables.dates.deadlineOffset);
        } else {
            event.to = new Date(event.to);
        }
    }

    return new Promise((resolve, rejct) => {
        //Save Event to database
        new eventModel(event).save((err, result) => {
            if (err) reject(err);
            resolve(result);
        });
    });
}