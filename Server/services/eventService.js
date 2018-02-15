const eventModel = require("../data/models/eventModel");
const userModel = require("../data/models/userModel");
const eventService = module.exports = {};
const eventVariables = require('../variables/EventVariables');
const Transaction = require('mongoose-transactions');
var ical = require('ical')

eventService.executeImport = (url, userId, index) => {
    return new Promise((resolve, reject) => {
        const options = {};
        ical.fromURL(url, options, (err, data) => {
            if (err) {
                reject("Invalid request");
            }

            if (data) {
                //Store new ones to database
                saveIcalEvents(data, userId, index).then(() => {
                    resolve();
                }).catch(e => {
                    reject(e);
                });
            }
        });
    });
}

function saveIcalEvents(events, userId, index) {
    return new Promise((resolve, reject) => {
        async function start() {
            try {
                //Delete all previous versions

                await eventModel.remove({
                    author: userId,
                    icalid: {
                        $ne: null
                    }
                });

                const eventsToStore = [];
                for (const [key, value] of Object.entries(events)) {
                    if (!key) {
                        throw new Error("One or multiple values had no ID!");
                    }

                    let summary = null;
                    let description = null;
                    let location = null;

                    if (value.summary) {
                        summary = extractStringFromIcal(value.summary).substring(0, eventVariables.summary.maxLength - 1);
                    }
                    if (value.description) {
                        description = extractStringFromIcal(value.description).substring(0, eventVariables.description.maxLength - 1);
                    }
                    if (value.location) {
                        location = extractStringFromIcal(value.location).substring(0, eventVariables.location.maxLength - 1);
                    }

                    if (value.start && value.end) {
                        //Construct event from data and store it
                        const event = {
                            author: userId,
                            icalid: key,
                            summary: summary,
                            description: description,
                            location: location,
                            type: 0,
                            from: new Date(value.start).getTime(),
                            to: new Date(value.end).getTime()
                        }
                        eventsToStore.push(event);
                    }
                }

                const results = await eventModel.create(eventsToStore);
                const indexObjects = [];
                results.forEach(result => {
                    indexObjects.push({
                        author: result.author,
                        objectID: result.icalid,
                        summary: result.summary,
                        description: result.description,
                        location: result.location,
                        type: result.type,
                        from: result.from,
                        to: result.to,
                        eventID: result.id,
                    })
                });
                index.addObjects(indexObjects);
                resolve();
            } catch (e) {
                reject(e);
            }
        }
        start();
    })
}

function extractStringFromIcal(element) {
    let result = null;

    if (typeof (element) == "string") {
        result = element;
    } else if (element && element.val) {
        result = element.val;
    }

    return result;
}

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
        if ((event.type && event.type === eventVariables.types.deadline) || (event.from.getUTCMilliseconds() > event.to)) {
            event.to = new Date(event.from.getUTCMilliseconds() + eventVariables.dates.deadlineOffset);
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