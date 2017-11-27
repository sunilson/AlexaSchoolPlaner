var eventService = module.exports = {};
var eventModel = require("../data/models/eventModel");
var userModel = require("../data/models/userModel");
var eventVariables = require('../variables/EventVariables');

eventService.saveEvent = (event, userId) => {

    return new Promise((resolve, reject) => {

        if (!event) {
            reject({
                status: 400,
                message: "Event not valid!"
            });
        }

        //Check if from Date is given and is a positive Integer, also if to date is given it needs to be an Integer
        if (!event.from || !Number.isInteger(event.from) ||
            event.from < 0 || (event.to && !Number.isInteger(event.to))) {
            reject({
                status: 400,
                message: "Date was invalid"
            });
        }

        //Parse from Date
        event.from = new Date(event.from);

        //If event is a deadline, offset to date by 1 hour
        //Also if to Date is smaller than the from date offset it
        if ((event.type && event.type === eventVariables.types.deadline) || (event.from.getMilliseconds() > event.to)) {
            event.to = new Date(event.from.getMilliseconds() + eventVariables.dates.deadlineOffset);
        } else {
            event.to = new Date(event.to);
        }

        //Add author
        event.author = userId;

        new eventModel(event).save().then((result) => {
            resolve(result);
        }).catch((error) => {
            reject(error);
        });
    });
}