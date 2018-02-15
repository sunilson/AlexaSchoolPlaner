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
const request = require('request-promise-native');
const moment = require("moment");
const APP_ID = 'amzn1.ask.skill.db109c0a-8afb-4112-a995-8efc6c392dab'
const currentAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVhMTAzZTY5OWMwNDEyNGQyODEzNjkzYSIsImlhdCI6MTUxODI3MTg4MiwiZXhwIjoxNjA0NjcxODgyfQ.AGkPs3Ae96BhImcYryu1pe6H843lmE_yWLA5_VZKqD8"

const states = {
    ADDEVENTMODE: '_ADDEVENTMODE'
};

const questionTypes = {
    EVENTTYPE: "eventtype",
    STARTDATE: "startdate",
    ENDDATE: "enddate",
    STARTTIME: "starttime",
    ENDTIME: "endtime",
    SUMMARY: "summary",
    DESCRIPTION: "description"
};

const eventTypes = {
    "Schulstunde": 0,
    "Termin": 1,
    "Abgabe": 2
}

const languageStrings = {
    "en": {
        "translation": {
            SKILL_NAME: 'Schulplaner'
        }
    },
    "de": {
        "translation": {
            "Event": {
                NEXT_EVENT_DATE: "Dein nächstes Event ist am <say-as interpret-as='date'>",
                NO_NEXT_EVENT: "Du hast kein Event in nächster Zeit!",
                NEXT_EVENT_CARD_TITLE: "Dein nächstes Event"
            },
            "Schulstunde": {
                NEXT_EVENT_DATE: "Deine nächste Schulstunde ist am <say-as interpret-as='date'>",
                NO_NEXT_EVENT: "Du hast keine Schulstunde in nächster Zeit!",
                NEXT_EVENT_CARD_TITLE: "Deine nächste Schulstunde"
            },
            "Termin": {
                NEXT_EVENT_DATE: "Dein nächster Termin ist am <say-as interpret-as='date'>",
                NO_NEXT_EVENT: "Du hast keinen Termin in nächster Zeit!",
                NEXT_EVENT_CARD_TITLE: "Dein nächster Termin"
            },
            "Abgabe": {
                NEXT_EVENT_DATE: "Deine nächste Abgabe ist am <say-as interpret-as='date'>",
                NO_NEXT_EVENT: "Du hast keine Abgabe in nächster Zeit!",
                NEXT_EVENT_CARD_TITLE: "Deine nächste Abgabe",
            },
            SKILL_NAME: 'Schulplaner',
            YOU_HAVE_EVENT_AT_DATE: " hast du das nächste mal am <say-as interpret-as='date'>",
            TODAY_SCHOOL_EVENTS: "<s>Heute hast do folgende Schultermine: </s>",
            WITH_SUMMARY: " mit der Zusammenfassung",
            GET_FACT_MESSAGE: 'Hier sind deine Fakten: ',
            HELP_MESSAGE: 'Du kannst sagen, „Nenne mir einen Fakt über den Weltraum“, oder du kannst „Beenden“ sagen... Wie kann ich dir helfen?',
            HELP_REPROMPT: 'Wie kann ich dir helfen?',
            STOP_MESSAGE: 'Auf Wiedersehen!',
            DEFAULT_QUERY_ERROR: "Bei deiner Anfrage ist ein Fehler aufgetreten. Bitte versuche es so später erneut!",
            ASK_EVENTTYPE: "Ist das Event eine Schulstunde, ein Termin, oder eine Abgabe?",
            ASK_STARTDATE: "Wann startet das Event?",
            ASK_STARTDATE_MISSING: "An welchem Datum?",
            ASK_ENDDATE: "Wann endet das Event?",
            ASK_ENDDATE_OVERLAP: "End Datum liegt vor Start Datum. Bitte erneut versuchen!",
            ERROR_QUESTION_WITH_TEXT: "Sorry, die Antwort passt nicht zur Frage. Bitte gib einen gültigen Text an!",
            ASK_TIME: "Zu welcher Uhrzeit?",
            ERROR_QUESTION_WITH_DATE: "Sorry, die Antwort passt nicht zur Frage. Bitte gib ein gültiges Datum an!",
            ASK_SUMMARY: "Wie lautet die Zusammenfassung des Events?",
            ERROR_QUESTION_WITH_TIME: "Sorry, die Antwort passt nicht zur Frage. Bitte gib eine gültige Zeit an!",
            ERROR_QUESTION_WITH_DATE_TIME: "Sorry, die Antwort passt nicht zur Frage. Bitte gib ein gültiges Datum und/oder eine gültige Zeit an!",
            ERROR_GENERAL: "Sorry, die Antwort passt nicht zur Frage. Bitte versuche es noch einmal.",
            ICAL_INFO: "Sie können bestehende Kalenderdaten in der Schulplaner App unter den Einstellungen importieren",
            NO_SCHOOL_APPOINTMENTS_TODAY: "Heute hast du keine Schultermine!",
            TODAYS_SCHOOL_APPOINTMENTS_CARD_TITLE: "Deine heutigen Schultermine",
            DEADLINE: "Abgabe",
            APPOINTMENT: "Termin",
            SCHULFACH: "Schulfach",
            ANYTHING_ELSE: "Sonst noch etwas?",
            UNHANDLED: "Sorry, das habe ich nicht verstanden. Bitte versuche es noch einmal.",
            NO_LOCATION_GIVEN: "Sie haben keinen Ort angegeben. Bitte erneut versuchen."
        }
    }
}

const handlers = {
    'LaunchRequest': function () {
        this.emit(":ask", "Willkommen zu " + this.t("SKILL_NAME") + ". Was möchtest du tun?");
    },
    'NoEvent': function () {
        this.emit(':tell', this.t('STOP_MESSAGE'));
    },
    'ContinueSearch': function () {
        if (this.attributes["searchType"] && this.attributes["searchDate"]) {
            switch (this.attributes["searchType"]) {

            }
            this.emit('GetNextEvent');
        } else {
            this.emit(':tell', this.t("DEFAULT_QUERY_ERROR"));
        }

    },
    'GetEvent': function () {

        if (!this.event.request.intent.slots.Type || !this.event.request.intent.slots.Type.value) {
            this.emit(':tell', this.t("DEFAULT_QUERY_ERROR"));
        }

        const query = this.event.request.intent.slots.Type.value;
    },
    'GetNextEvent': function () {

        let type;
        let date;
        (this.attributes["searchDate"]) ? date = this.attributes["searchDate"]: date = Date.now();
        if (this.event.request.intent.slots &&
            this.event.request.intent.slots.Type &&
            this.event.request.intent.slots.Type.value &&
            validEventType(this.event.request.intent.slots.Type.value)) {
            type = this.event.request.intent.slots.Type.value
        } else {
            type = "Event"
        }
        this.attributes["searchType"] = type;

        simpleEventNetworkRequest(HOME_URL + "/events/nearestEvent?date=" + date + ((type != "Event") ? ("&type=" + eventTypes[type]) : "")).then((response) => {
            console.log(response)

            if (!response || response.length == 0) {
                this.emit(':tell', this.t(type)["NO_NEXT_EVENT"]);
            } else {
                let fromDate = formatDate(response.from)
                let fromTime = formatTime(response.from)
                this.attributes["searchDate"] = response.from

                this.emit(":ask", "<s>" + this.t(type)["NEXT_EVENT_DATE"] + fromDate.toString() + "</say-as> um " + fromTime.toString() + "</s> <s>Zusammenfassung des Events: </s><s>" + response.summary + "</s>" + this.t("ANYTHING_ELSE"))

                this.emit(':askwithcard',
                    "<s>" + this.t(type)["NEXT_EVENT_DATE"] + fromDate.toString() + "</say-as> um " + fromTime.toString() + "</s> <s>Zusammenfassung des Events: </s><s>" + response.summary + "</s>" + this.t("ANYTHING_ELSE"),
                    this.t(type)["NEXT_EVENT_CARD_TITLE"],
                    formatEventForCardContent(response)
                );
            }
        }).catch((e) => {
            console.log(e)
            this.emit(':tellwithcard',
                this.t("DEFAULT_QUERY_ERROR"),
                this.t(type)["NEXT_EVENT_CARD_TITLE"],
                this.t("DEFAULT_QUERY_ERROR")
            );
        });
    },
    'GetTodaysSchoolAppointments': function () {
        let fromDate = moment().startOf('day');
        let toDate = moment().endOf('day');

        var options = {
            uri: HOME_URL + "/events?from=" + fromDate.valueOf() + "&to=" + toDate.valueOf() + "&type=0",
            headers: {
                'User-Agent': 'Request-Promise',
                Authorization: 'Bearer ' + currentAccessToken
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
                this.emit(':tellwithcard',
                    result,
                    this.t("TODAYS_SCHOOL_APPOINTMENTS_CARD_TITLE"),
                    result);
            } else {
                this.emit(':tellwithcard',
                    this.t("NO_SCHOOL_APPOINTMENTS_TODAY"),
                    this.t("TODAYS_SCHOOL_APPOINTMENTS_CARD_TITLE"),
                    this.t("NO_SCHOOL_APPOINTMENTS_TODAY")
                );
            }
        }).catch((e) => {
            this.emit(':tellwithcard',
                this.t("DEFAULT_QUERY_ERROR"),
                this.t("TODAYS_SCHOOL_APPOINTMENTS_CARD_TITLE"),
                this.t("DEFAULT_QUERY_ERROR")
            );
        });
    },
    'GetNextEventAtLocation': function () {

        let date;
        (this.attributes["searchDate"]) ? date = this.attributes["searchDate"]: date = Date.now();
        this.attributes["searchType"] = searchType.NEXT_APPOINTMENT;

        if (this.event.request.intent && (!this.event.request.intent.slots.query || !this.event.request.intent.slots.query.value)) {
            this.emit(':ask', this.t("NO_LOCATION_GIVEN"));
        } else {
            this.attributes["searchLocation"] = this.event.request.intent.slots.query.value
        }

        simpleEventNetworkRequest(HOME_URL + "/events/searchNextEventAtLocation?query=" + this.event.request.intent.slots.query.value + "&date=" + date).then((response) => {
            if (!response || response.length == 0) {
                this.emit(':tell', this.t("NO_NEXT_EVENT_AT_LOCATION"));
                return;
            }

            let fromDate = formatDate(response.from)
            let fromTime = formatTime(response.from);
            this.attributes["searchDate"] = response.from;

            this.emit(':askwithcard',
                "<s>" + this.t("APPOINTMENT") + " " + this.t("WITH_SUMMARY") + response.summary + this.t("NEXT_SCHOOL_EVENT") + " " + fromDate.toString() + "</say-as> um " + fromTime.toString() + "</s>" + this.t("ANYTHING_ELSE"),
                this.t("NEXT_APPOINTMENT_CARD_TITLE"),
                formatEventForCardContent(response)
            );
        }).catch((e) => {
            this.emit(':tellwithcard',
                this.t("NO_NEXT_APPOINTMENT"),
                this.t("NEXT_APPOINTMENT_CARD_TITLE"),
                this.t("NO_NEXT_APPOINTMENT")
            );
        });
    },
    'GetIcalInfo': function () {
        this.emit(':tell', this.t("ICAL_INFO"));
    },
    'AddEvent': function () {
        if (this.event.request.intent && (!this.event.request.intent.slots.EventType || !this.event.request.intent.slots.EventType.value)) {
            this.handler.state = states.ADDEVENTMODE;
            this.attributes["questionType"] = questionTypes.EVENTTYPE;
            this.emit(':ask', this.t("ASK_EVENTTYPE"));
        } else if (this.event.request.intent && this.event.request.intent.slots.EventType && this.event.request.intent.slots.EventType.value) {
            this.handler.state = states.ADDEVENTMODE;
            this.attributes[questionTypes.EVENTTYPE] = this.event.request.intent.slots.EventType.value;
            this.attributes["questionType"] = questionTypes.STARTDATE;
            this.emit(":ask", this.t("ASK_STARTDATE"));
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
        this.emit(':ask', this.t("UNHANDLED"));
    }
};

//Events used when user wants to add an event. Will only be accessed when in "ADDEVENT" state
var addEventHandlers = Alexa.CreateStateHandler(states.ADDEVENTMODE, {
    'QuestionAnsweredWithEventType': function () {
        this.attributes["eventType"] = this.event.request.intent.slots.EventType.value;
        this.attributes["questionType"] = questionTypes.STARTDATE;
        this.emit(':ask', this.t("ASK_STARTDATE"));
    },
    'QuestionAnsweredWithText': function () {
        switch (this.attributes["questionType"]) {
            case questionTypes.SUMMARY:
                this.emit(':ask', 'Summary!');
                break;
            default:
                this.emit(':ask', this.t("ERROR_QUESTION_WITH_TEXT"));
                break;
        }
    },
    'QuestionAnsweredWithDate': function () {
        if (!this.event.request.intent.slots.Date || !this.event.request.intent.slots.Date.value) {
            this.emit(':ask', this.t("ERROR_QUESTION_WITH_DATE"));
        } else {
            switch (this.attributes["questionType"]) {
                case questionTypes.STARTDATE:
                    this.attributes[questionTypes.STARTDATE] = this.event.request.intent.slots.Date.value;
                    this.attributes["questionType"] = questionTypes.STARTTIME;
                    this.emit(':ask', this.t("ASK_TIME"));
                    break;
                case questionTypes.ENDDATE:
                    const value = this.event.request.intent.slots.Date.value;
                    //TODO Overlap check
                    break;
                default:
                    this.emit(':ask', this.t("ERROR_QUESTION_WITH_DATE"));
                    break;
            }
        }
    },
    'QuestionAnsweredWithDateTime': function () {
        if (!this.event.request.intent.slots.Date ||
            !this.event.request.intent.slots.Time ||
            !this.event.request.intent.slots.Date.value ||
            !this.event.request.intent.slots.Time.value) {
            this.emit(':ask', this.t("ERROR_QUESTION_WITH_DATE_TIME"));
        } else {
            switch (this.attributes["questionType"]) {
                case questionTypes.STARTDATE:
                    this.attributes[questionTypes.STARTDATE] = this.event.request.intent.slots.Date.value;
                    this.attributes[questionTypes.STARTTIME] = this.event.request.intent.slots.Time.value;
                    if (this.attributes["eventType"] != "deadline") {
                        this.attributes["questionType"] = questionTypes.ENDDATE;
                        this.emit(':ask', this.t("ASK_ENDDATE"));
                    } else {
                        this.attributes["questionType"] = questionTypes.SUMMARY;
                        this.emit(':ask', this.t("ASK_SUMMARY"));
                    }
                    break;
                case "endDate":
                    //TODO
                    //TODO Overlap check
                    break;
                default:
                    this.emit(':ask', this.t("ERROR_QUESTION_WITH_DATE_TIME"));
                    break;
            }
        }
    },
    'QuestionAnsweredWithTime': function () {
        if (!this.event.request.intent.slots.Time || !this.event.request.intent.slots.Time.value) {
            this.emit(':ask', this.t("ERROR_QUESTION_WITH_TIME"));
        } else {
            switch (this.attributes["questionType"]) {
                case questionTypes.STARTTIME:
                    this.attributes[questionTypes.STARTTIME] = this.event.request.intent.slots.Time.value;
                    if (!this.attributes[questionTypes.STARTDATE]) {
                        this.attributes["questionType"] = questionTypes.STARTDATE;
                        this.emit(':ask', this.t("ASK_STARTDATE_MISSING"));
                    } else if (this.attributes["eventType"] != "deadline") {
                        this.attributes["questionType"] = questionTypes.ENDDATE;
                        this.emit(':ask', this.t("ASK_ENDDATE"));
                    } else {
                        this.attributes["questionType"] = questionTypes.SUMMARY;
                        this.emit(':ask', this.t("ASK_SUMMARY"));
                    }
                    break;
                default:
                    this.emit(':ask', this.t("ERROR_QUESTION_WITH_TIME"));
                    break;
            }
        }
    },
    'Unhandled': function () {
        this.emit(':ask', this.t("ERROR_GENERAL"));
    },
});

function formatDate(date) {
    return moment(date).format('YYYYMMDD');
}

function formatTime(time) {
    return moment(time).format('hh:mm:ss');
}

function formatEventForCardContent(event) {
    let result = "";

    if (event.summary) {
        result += "Event Zusammenfassung: " + event.summary + " - "
    }

    if (event.description) {
        result += "Event Beschreibung: " + event.description + " - "
    }

    if (event.location) {
        result += "Event Location: " + event.location + " - "
    }

    if (event.from) {
        result += "Startet am: " + event.from + " - "
    }

    if (event.to) {
        result += "Endet am: " + event.to + " - "
    }

    return result
}

function validEventType(type) {
    for (let foundType in eventTypes) {
        if (type == foundType) return true
    }

    return false
}

function simpleEventNetworkRequest(uri) {
    const options = {
        uri: uri,
        headers: {
            'User-Agent': 'Request-Promise',
            Authorization: 'Bearer ' + currentAccessToken
        },
        json: true // Automatically parses the JSON string in the response
    };

    return request(options);
}

function checkOverlappingDates(startDate, endDate) {

}

exports.handler = function (event, context) {
    const alexa = Alexa.handler(event, context);
    if ('undefined' === typeof process.env.DEBUG) {
        alexa.APP_ID = APP_ID;
    }
    alexa.resources = languageStrings;
    alexa.registerHandlers(handlers, addEventHandlers);
    alexa.execute();
};