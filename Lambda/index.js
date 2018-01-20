/* eslint-disable  func-names */
/* eslint quote-props: ["error", "consistent"]*/
/**
 * This sample demonstrates a simple skill built with the Amazon Alexa Skills
 * nodejs skill development kit.
 * This sample supports multiple lauguages. (en-US, en-GB, de-DE).
 * The Intent Schema, Custom Slots and Sample Utterances for this skill, as well
 * as testing instructions are located at https://github.com/alexa/skill-sample-nodejs-fact
 **/

'use strict';

const Alexa = require('alexa-sdk');
const HOME_URL = "http://localhost:5000"
var request = require('request-promise-native');
var moment = require("moment");

const APP_ID = undefined; // TODO replace with your app ID (OPTIONAL).

var states = {
    ADDEVENTMODE: '_ADDEVENTMODE'
};

var questionTypes = {
    EVENTTYPE: "eventtype",
    STARTDATE: "startdate",
    ENDDATE: "enddate",
    STARTTIME: "starttime",
    ENDTIME: "endtime",
    SUMMARY: "summary",
    DESCRIPTION: "description"
};

const languageStrings = {
    standard: {
        SKILL_NAME: 'Schulplaner',
        NEXT_EVENT_DATE: "Dein nächstes Event ist am <say-as interpret-as='date'>",
        NEXT_SCHOOL_EVENT: " hast du das nächste mal am <say-as interpret-as='date'>",
        TODAY_SCHOOL_EVENTS: "<s>Heute hast do folgende Schultermine: </s>",
        WITH_SUMMARY: " mit der Zusammenfassung",
        TYPES: [
            'Schultermin',
            'Termin',
            'Abgabe Event'
        ],
        GET_FACT_MESSAGE: 'Hier sind deine Fakten: ',
        HELP_MESSAGE: 'Du kannst sagen, „Nenne mir einen Fakt über den Weltraum“, oder du kannst „Beenden“ sagen... Wie kann ich dir helfen?',
        HELP_REPROMPT: 'Wie kann ich dir helfen?',
        STOP_MESSAGE: 'Auf Wiedersehen!',
    },
    addEvent: {
        ASK_EVENTTYPE: "Ist das Event eine Schulstunde, ein Termin, oder eine Abgabe?",
        ASK_STARTDATE: "Wann startet das Event?",
        ASK_STARTDATE_MISSING: "An welchem Datum?",
        ASK_ENDDATE: "Wann endet das Event?",
        ERROR_QUESTION_WITH_TEXT: "Sorry, die Antwort passt nicht zur Frage. Bitte gib einen gültigen Text an!",
        ASK_TIME: "Zu welcher Uhrzeit?",
        ERROR_QUESTION_WITH_DATE: "Sorry, die Antwort passt nicht zur Frage. Bitte gib ein gültiges Datum an!",
        ASK_SUMMARY: "Wie lautet die Zusammenfassung des Events?",
        ERROR_QUESTION_WITH_TIME: "Sorry, die Antwort passt nicht zur Frage. Bitte gib eine gültige Zeit an!",
        ERROR_QUESTION_WITH_DATE_TIME: "Sorry, die Antwort passt nicht zur Frage. Bitte gib ein gültiges Datum und/oder eine gültige Zeit an!",
        ERROR_GENERAL: "Sorry, die Antwort passt nicht zur Frage. Bitte versuche es noch einmal."
    }
}

const handlers = {
    'LaunchRequest': function () {
        this.emit(":ask", "Willkommen zu " + languageStrings.standard.SKILL_NAME + ". Was möchtest du tun?");
    },
    'GetNewFactIntent': function () {
        this.emit('GetFact');
    },
    'GetFact': function () {
        // Get a random space fact from the space facts list
        // Use this.t() to get corresponding language data
        const factArr = this.t('FACTS');
        const factIndex = Math.floor(Math.random() * factArr.length);
        const randomFact = factArr[factIndex];

        // Create speech output
        const speechOutput = this.t('GET_FACT_MESSAGE') + randomFact;
        this.emit(':tellWithCard', speechOutput, this.t('SKILL_NAME'), randomFact);
    },
    'GetNextEvent': function () {

        var options = {
            uri: HOME_URL + "/events/nearestEvent?date=" + Date.now(),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true // Automatically parses the JSON string in the response
        };

        request(options).then((response) => {

            let fromDate = moment(response.from).format('YYYYMMDD');
            let fromTime = moment(response.from).format('hh:mm:ss');

            this.emit(':tellWithCard',
                "<s>" + this.t("NEXT_EVENT_DATE") + fromDate.toString() + "</say-as> um " + fromTime.toString() + "</s>Es ist ein " + this.t("TYPES")[response.type] + " Event. <s>Zusammenfassung des Events: </s><s>" + response.summary + "</s>", this.t('SKILL_NAME'), "blub");
        }).catch((e) => {
            this.emit(':tellWithCard', "Du hast in den nächsten 3 Stunden kein Event!" + e, this.t('SKILL_NAME'), "blub");
        });
    },
    'GetNextSchoolAppointment': function () {

        var options = {
            uri: HOME_URL + "/events/searchNextEvent?type=0&query=" + this.event.request.intent.slots.Schulfach.value,
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true // Automatically parses the JSON string in the response
        };

        request(options).then((response) => {
            let fromDate = moment(response.from).format('YYYYMMDD');
            let fromTime = moment(response.from).format('hh:mm:ss');

            this.emit(':tellWithCard', "<s>" + this.event.request.intent.slots.Schulfach.value + this.t("WITH_SUMMARY") + response.summary + this.t("NEXT_SCHOOL_EVENT") + fromDate.toString() + "</say-as> um " + fromTime.toString() + "</s>");
        }).catch((e) => {
            this.emit(':tell', "yo");
        });
    },
    'GetTodaysSchoolAppointments': function () {
        let fromDate = moment().startOf('day');
        let toDate = moment().endOf('day');

        var options = {
            uri: HOME_URL + "/events?from=" + fromDate.valueOf() + "&to=" + toDate.valueOf() + "&type=0",
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true // Automatically parses the JSON string in the response
        };

        request(options).then((response) => {
            if (response && response.length > 0) {
                let result = this.t("TODAY_SCHOOL_EVENTS");
                for (let event of response) {
                    let fromTime = moment(event.from).format('hh:mm:ss');
                    result += "<s>" + event.summary;
                    if (event.description) {
                        result += " mit Beschreibung " + event.description;
                    }
                    result += " um " + fromTime.toString() + "</s>"
                }

                this.emit(':tellWithCard', result, this.t('SKILL_NAME'), "blub");
            } else {

            }

            //this.emit(':tell', "ya");
            this.emit(':tellWithCard', "<s>" + this.event.request.intent.slots.Schulfach.value + this.t("WITH_SUMMARY") + response.summary + this.t("NEXT_SCHOOL_EVENT") + fromDate.toString() + "</say-as> um " + fromTime.toString() + "</s>");
        }).catch((e) => {
            console.log(e);
            this.emit(':tell', "yo");
        });
    },
    'AddEvent': function () {

        if (this.event.request.intent && this.event.request.intent.slots.EventType && !this.event.request.intent.slots.EventType.value) {
            this.handler.state = states.ADDEVENTMODE;
            this.attributes["questionType"] = questionTypes.EVENTTYPE;
            this.emit(':ask', languageStrings.addEvent.ASK_EVENTTYPE);
        } else if (this.event.request.intent && this.event.request.intent.slots.EventType && this.event.request.intent.slots.EventType.value) {
            this.attributes["questionType"] = questionTypes.STARTDATE;
            this.emit(":ask", languageStrings.addEvent.ASK_STARTDATE);
        }
    },
    'AMAZON.HelpIntent': function () {
        const speechOutput = this.t('HELP_MESSAGE');
        const reprompt = this.t('HELP_MESSAGE');
        this.emit(':ask', speechOutput, reprompt);
    },
    'AMAZON.CancelIntent': function () {
        this.emit(':tell', this.t('STOP_MESSAGE'));
    },
    'AMAZON.StopIntent': function () {
        this.emit(':tell', this.t('STOP_MESSAGE'));
    },
    'Unhandled': function () {
        this.emit(':ask', 'Sorry, I didn\'t get that. Try saying a number.', 'Try saying a number.');
    }
};

var addEventHandlers = Alexa.CreateStateHandler(states.ADDEVENTMODE, {
    'QuestionAnsweredWithEventType': function () {
        this.attributes["eventType"] = this.event.request.intent.slots.EventType.value;
        this.attributes["questionType"] = questionTypes.STARTDATE;
        this.emit(':ask', languageStrings.addEvent.ASK_STARTDATE);
    },
    'QuestionAnsweredWithText': function () {
        switch (this.attributes["questionType"]) {
            case questionTypes.SUMMARY:
                this.emit(':ask', 'Summary!');
                break;
            default:
                this.emit(':ask', languageStrings.addEvent.ERROR_QUESTION_WITH_TEXT);
                break;
        }
    },
    'QuestionAnsweredWithDate': function () {
        if (!this.event.request.intent.slots.Date || !this.event.request.intent.slots.Date.value) {
            this.emit(':ask', languageStrings.addEvent.ERROR_QUESTION_WITH_DATE);
        } else {
            switch (this.attributes["questionType"]) {
                case questionTypes.STARTDATE:
                    this.attributes["startDate"] = this.event.request.intent.slots.Date.value;
                    this.attributes["questionType"] = questionTypes.STARTTIME;
                    this.emit(':ask', languageStrings.addEvent.ASK_TIME);
                    break;
                case questionTypes.ENDDATE:
                    break;
                default:
                    this.emit(':ask', languageStrings.addEvent.ERROR_QUESTION_WITH_DATE);
                    break;
            }
        }
    },
    'QuestionAnsweredWithDateTime': function () {
        if (!this.event.request.intent.slots.Date ||
            !this.event.request.intent.slots.Time ||
            !this.event.request.intent.slots.Date.value ||
            !this.event.request.intent.slots.Time.value) {
            this.emit(':ask', languageStrings.addEvent.ERROR_QUESTION_WITH_DATE_TIME);
        } else {
            switch (this.attributes["questionType"]) {
                case questionTypes.STARTDATE:
                    this.attributes[questionTypes.STARTDATE] = this.event.request.intent.slots.Date.value;
                    this.attributes[questionTypes.STARTTIME] = this.event.request.intent.slots.Time.value;
                    if (this.attributes["eventType"] != "deadline") {
                        this.attributes["questionType"] = questionTypes.ENDDATE;
                        this.emit(':ask', languageStrings.addEvent.ASK_ENDDATE);
                    } else {
                        this.attributes["questionType"] = questionTypes.SUMMARY;
                        this.emit(':ask', languageStrings.addEvent.ASK_SUMMARY);
                    }
                    break;
                case "endDate":
                    break;
                default:
                    this.emit(':ask', languageStrings.addEvent.ERROR_QUESTION_WITH_TIME);
                    break;
            }
        }
    },
    'QuestionAnsweredWithTime': function () {
        if (!this.event.request.intent.slots.Time || !this.event.request.intent.slots.Time.value) {
            this.emit(':ask', languageStrings.addEvent.ERROR_QUESTION_WITH_TIME);
        } else {
            switch (this.attributes["questionType"]) {
                case questionTypes.STARTTIME:
                    this.attributes[questionTypes.STARTTIME] = this.event.request.intent.slots.Time.value;
                    if (!this.attributes[questionTypes.STARTDATE]) {
                        this.attributes["questionType"] = questionTypes.STARTDATE;
                        this.emit(':ask', languageStrings.addEvent.ASK_STARTDATE_MISSING);
                    } else if (this.attributes["eventType"] != "deadline") {
                        this.attributes["questionType"] = questionTypes.ENDDATE;
                        this.emit(':ask', languageStrings.addEvent.ASK_ENDDATE);
                    } else {
                        this.attributes["questionType"] = questionTypes.SUMMARY;
                        this.emit(':ask', languageStrings.addEvent.ASK_SUMMARY);
                    }
                    break;
            }
        }
    },
    'Unhandled': function () {
        this.emit(':ask', languageStrings.addEvent.ERROR_GENERAL);
    },
});

exports.handler = function (event, context) {
    const alexa = Alexa.handler(event, context);
    alexa.APP_ID = APP_ID;
    alexa.registerHandlers(handlers, addEventHandlers);
    alexa.execute();
};