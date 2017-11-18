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
        } else {
            reject({
                status: 400,
                message: "Date was invalid"
            });
        }

        //TODO User suchen und Event pushen
        userModel.find({
            _id: userId
        }).exec().then((user) => {
            user.events.push(new eventModel({

            }));
            return user.save();
        }).then((result) => {
            resolve(result);
        }).catch((error) => {
            reject(error);
        });
    });
}