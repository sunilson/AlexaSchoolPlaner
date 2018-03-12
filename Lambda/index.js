'use strict';

const Alexa = require('alexa-sdk');
const HOME_URL = "https://bachelorthesis17.herokuapp.com"
const request = require('request-promise-native');
const moment = require("moment");
const APP_ID = 'amzn1.ask.skill.db109c0a-8afb-4112-a995-8efc6c392dab'
const currentAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVhMTAzZTY5OWMwNDEyNGQyODEzNjkzYSIsImlhdCI6MTUxODcwODE5MiwiZXhwIjoxNjA1MTA4MTkyfQ.SPoZfImr2EYtLEY5bn0YW1tQocuLhSr5eqk92C0pjKY"
const AmazonDateParser = require('amazon-date-parser');

const states = {
    ADDEVENTMODE: '_ADDEVENTMODE',
    NEXTEVENTMODE: '_NEXTEVENTMODE',
    DEFAULTMODE: '_DEFAULTMODE'
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
            NEXT_EVENTS: "Du hast mehrere Events am <say-as interpret-as='date'>",
            "Event": {
                EVENT: "Ein Event",
                NEXT_EVENT_DATE: "Dein nächstes Event ist am <say-as interpret-as='date'>",
                NO_NEXT_EVENT: "Du hast kein Event in nächster Zeit!",
                NEXT_EVENT_CARD_TITLE: "Dein nächstes Event",
                TODAY_EVENTS: "Hier sind die Events am Datum <say-as interpret-as='date'>",
                NO_NEXT_EVENT_IN_LIMIT: "Du hast kein Event zu dieser Zeit"
            },
            "Schulstunde": {
                EVENT: "Eine Schulstunde",
                NEXT_EVENT_DATE: "Deine nächste Schulstunde ist am <say-as interpret-as='date'>",
                NO_NEXT_EVENT: "Du hast keine Schulstunde in nächster Zeit!",
                NEXT_EVENT_CARD_TITLE: "Deine nächste Schulstunde",
                TODAY_EVENTS: "Hier sind die Schulstunden am Datum <say-as interpret-as='date'>",
                NO_NEXT_EVENT_IN_LIMIT: "Du hast keine Schulstunde zu dieser Zeit"
            },
            "Termin": {
                EVENT: "Ein Termin",
                NEXT_EVENT_DATE: "Dein nächster Termin ist am <say-as interpret-as='date'>",
                NO_NEXT_EVENT: "Du hast keinen Termin in nächster Zeit!",
                NEXT_EVENT_CARD_TITLE: "Dein nächster Termin",
                TODAY_EVENTS: "Hier sind die Termine am Datum <say-as interpret-as='date'>",
                NO_NEXT_EVENT_IN_LIMIT: "Du hast keinen Termin zu dieser Zeit"
            },
            "Abgabe": {
                EVENT: "Eine Abgabe",
                NEXT_EVENT_DATE: "Deine nächste Abgabe ist am <say-as interpret-as='date'>",
                NO_NEXT_EVENT: "Du hast keine Abgabe in nächster Zeit!",
                NEXT_EVENT_CARD_TITLE: "Deine nächste Abgabe",
                TODAY_EVENTS: "Hier sind die Abgaben am Datum <say-as interpret-as='date'>",
                NO_NEXT_EVENT_IN_LIMIT: "Du hast keine Abgabe zu dieser Zeit"
            },
            SKILL_NAME: 'Schulplaner',
            YOU_HAVE_EVENT_AT_DATE: " hast du das nächste mal am <say-as interpret-as='date'>",
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
            ASK_DESCRIPTION: "Was für eine Beschreibung soll das Event haben?",
            ERROR_QUESTION_WITH_TEXT: "Sorry, die Antwort passt nicht zur Frage. Bitte gib einen gültigen Text an!",
            ASK_TIME: "Zu welcher Uhrzeit?",
            ERROR_QUESTION_WITH_DATE: "Sorry, die Antwort passt nicht zur Frage. Bitte gib ein gültiges Datum an!",
            ASK_SUMMARY: "Wie lautet die Zusammenfassung des Events?",
            ERROR_QUESTION_WITH_TIME: "Sorry, die Antwort passt nicht zur Frage. Bitte gib eine gültige Zeit an!",
            ERROR_QUESTION_WITH_DATE_TIME: "Sorry, die Antwort passt nicht zur Frage. Bitte gib ein gültiges Datum und/oder eine gültige Zeit an!",
            ERROR_QUESTION_WITH_DATE_TIME_OVERLAP: "Sorry, das Start- und Enddatum scheinen sich zu überlappen. Bitte versuche es noch einmal!",
            ERROR_GENERAL: "Sorry, die Antwort passt nicht zur Frage. Bitte versuche es noch einmal.",
            ICAL_INFO: "Sie können bestehende Kalenderdaten in der Schulplaner App unter den Einstellungen importieren",
            NO_SCHOOL_APPOINTMENTS_TODAY: "Heute hast du keine Schultermine!",
            TODAYS_SCHOOL_APPOINTMENTS_CARD_TITLE: "Deine heutigen Schultermine",
            DEADLINE: "Abgabe",
            APPOINTMENT: "Termin",
            SCHULFACH: "Schulfach",
            ANYTHING_ELSE: "Sonst noch etwas?",
            ASK_CONTINUE: 'Sage <s>Weiter!</s> um das nächste Event abzufragen oder <s>Abbrechen!</s> um zum Start zurückzukehren. <amazon:effect name="whispered">Übrigens, Sarah stinkt extrem!</amazon:effect> Sie ist so fucking scheiße Lol!',
            UNHANDLED: "Sorry, das habe ich nicht verstanden. Bitte versuche es noch einmal.",
            NO_LOCATION_GIVEN: "Sie haben keinen Ort angegeben. Bitte erneut versuchen.",
            REPROMPT: "Sind Sie noch da?",
            UNHANDLED_STATE: "Diese Frage passt nicht. Mit <s>Abbrechen!</s> kehrst du zum Start zurück.",
            IN_LOCATION: "in"
        }
    }
}

const newSessionHandlers = {
    'LaunchRequest': function () {

        //Ask for user login if no token is here
        this.emit(':tellWithLinkAccountCard',
            'to start using this skill, please use the companion app to authenticate on Amazon');
        return;

        this.handler.state = states.DEFAULTMODE;
        this.emit(":ask", "Willkommen zu " + this.t("SKILL_NAME") + ". Was möchtest du tun?");
    },
};

const defaultHandlers = Alexa.CreateStateHandler(states.DEFAULTMODE, {
    'NoEvent': function () {
        this.emit(':tell', this.t('STOP_MESSAGE'));
    },
    'GetEvent': function () {
        if (!this.event.request.intent.slots.Type || !this.event.request.intent.slots.Type.value) {
            this.emit(':tell', this.t("DEFAULT_QUERY_ERROR"));
        }
        const query = this.event.request.intent.slots.Type.value;
    },
    'SearchNextEvent': function () {
        let query = null
        if (this.event.request.intent.slots.Query) {
            this.attributes["query"] = extractDateRange(this.event.request.intent.slots)
        }

        let dates = null
        if (this.event.request.intent.slots.Date) {
            dates = extractDateRange(this.event.request.intent.slots)
        }
        let time = null
        if (this.event.request.intent.slots.Time) {
            time = extractTime(this.event.request.intent.slots)
            if (dates) {
                const date1 = moment(dates[0])
                const date2 = moment(dates[1])
                date1.set({
                    'hour': time.get('hour'),
                    'minute': time.get('minute'),
                    'second': time.get('second'),
                    'millisecond': time.get('millisecond')
                })
                date2.set({
                    'hour': time.get('hour'),
                    'minute': time.get('minute'),
                    'second': time.get('second'),
                    'millisecond': time.get('millisecond')
                })
                this.attributes["dateTime"] = date1
                this.attributes["limitDateTime"] = date2
            } else {
                this.attributes["dateTime"] = time
                this.attributes["limitDateTime"] = time.add(1, "hour")
            }
        } else {
            if (dates) {
                this.attributes["dateTime"] = moment(dates[0]);
                this.attributes["limitDateTime"] = moment(dates[1]);
            }
        }

        this.handler.state = states.NEXTEVENTMODE;
        this.emitWithState('ExecuteGetNextEvent');
    },
    'GetNextEvent': function () {
        if (this.event.request.intent && this.event.request.intent.slots.Location && this.event.request.intent.slots.Location.value) this.attributes["location"] = this.event.request.intent.slots.Location.value
        this.handler.state = states.NEXTEVENTMODE;
        this.emitWithState('ExecuteGetNextEvent');
    },
    'GetNextEventAtDateTime': function () {
        let fromDateTime;
        this.attributes["limit"] = true;

        //Get DateTime of given date and time
        if (this.event.request.intent.slots) {
            let date;
            let time;

            if (this.event.request.intent.slots.Date && this.event.request.intent.slots.Date.value) {
                let tempDates = new AmazonDateParser(this.event.request.intent.slots.Date.value)
                date = tempDates["startDate"]
                fromDateTime = moment(date);
            }

            if (this.event.request.intent.slots.Time && this.event.request.intent.slots.Time.value) {
                time = extractTime(this.event.request.intent.slots)
                if (date) {
                    date.set({
                        'hour': time.get('hour'),
                        'minute': time.get('minute'),
                        'second': time.get('second'),
                        'millisecond': time.get('millisecond')
                    })
                    fromDateTime = moment(date);
                } else {
                    fromDateTime = time
                }
            } else {
                this.attributes["limit"] = false;
            }
        }

        if (fromDateTime) this.attributes["dateTime"] = fromDateTime.valueOf()
        this.handler.state = states.NEXTEVENTMODE;
        this.emitWithState('ExecuteGetNextEvent');
    },
    'GetEventsAtDate': function () {
        //Check if a certain type is given. Otherwise just search for all events
        let type = extractType(this.event.request.intent.slots)

        let extractedDates = extractDateRange(this.event.request.intent.slots)

        var options = {
            uri: HOME_URL + "/events?from=" + extractedDates[0].valueOf() + "&to=" + extractedDates[1].valueOf() + ((type != "Event") ? ("&type=" + eventTypes[type]) : ""),
            headers: {
                'User-Agent': 'Request-Promise',
                Authorization: 'Bearer ' + currentAccessToken
            },
            json: true // Automatically parses the JSON string in the response
        };

        request(options).then((response) => {
            if (response && response.length > 0) {
                let result = this.t(type)["TODAY_EVENTS"] + formatDate(extractedDates[0]) + "</say-as>";
                for (let event of response) {
                    let fromTime = moment(event.from).format('hh:mm:ss');
                    result += "<s>" + event.summary;
                    if (event.description) {
                        result += " mit Beschreibung " + event.description;
                    }
                    result += " um " + fromTime.toString() + "</s>"
                }
                this.emit(':tellWithCard',
                    result,
                    this.t("TODAYS_SCHOOL_APPOINTMENTS_CARD_TITLE"),
                    result);
            } else {
                this.emit(':tellWithCard',
                    this.t("NO_SCHOOL_APPOINTMENTS_TODAY"),
                    this.t("TODAYS_SCHOOL_APPOINTMENTS_CARD_TITLE"),
                    this.t("NO_SCHOOL_APPOINTMENTS_TODAY")
                );
            }
        }).catch((e) => {
            this.emit(':tellWithCard',
                this.t("DEFAULT_QUERY_ERROR"),
                this.t("TODAYS_SCHOOL_APPOINTMENTS_CARD_TITLE"),
                this.t("DEFAULT_QUERY_ERROR")
            );
        });
    },
    'GetIcalInfo': function () {
        this.emit(':tell', this.t("ICAL_INFO"));
    },
    'AddEvent': function () {
        //Check for given slots and start add Event intent with correct state
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
});

const nextEventHandlers = Alexa.CreateStateHandler(states.NEXTEVENTMODE, {
    'LaunchRequest': function () {
        this.emit("ExecuteGetNextEvent");
    },
    'ExecuteGetNextEvent': function () {
        let date;
        let location = "";
        let excluded = "";

        //Check if a certain type is given. Otherwise just search for all events
        let type = extractType(this.event.request.intent.slots)

        //If this is a continous request, get previous date, also check if a start date is given
        if (this.attributes["searchDate"] && this.attributes["continue"]) {
            date = this.attributes["searchDate"]
        } else if (this.attributes["dateTime"]) {
            date = this.attributes["dateTime"]
        } else {
            date = Date.now();
        }

        //If this is a continous request, get excluded id's
        if (!this.attributes["continue"]) {
            this.attributes["excluded"] = []
        } else {
            excluded = "&excluded=" + JSON.stringify(this.attributes["excluded"])
        }

        //If location is given, add it to request
        if (this.attributes["location"]) {
            location = "&location=" + this.attributes["location"]
        }

        //TODO Max Date UND Query

        //Create network request
        simpleEventNetworkRequest(HOME_URL + "/events/nextEvent?date=" + date + excluded + location + "&limit=" + this.attributes["limit"] + ((type != "Event") ? ("&type=" + eventTypes[type]) : "")).then((response) => {
            //Check if results have been found
            if (!response || response.length == 0) {
                //Check if event time was limited
                if (this.attributes["limit"]) {
                    this.attributes["searchDate"] = this.attributes["dateTime"]
                    this.emit(':askWithCard',
                        this.t(type)["NO_NEXT_EVENT_IN_LIMIT"] + ". " + this.t("ASK_CONTINUE"),
                        this.t("REPROMPT"),
                        this.t(type)["NEXT_EVENT_CARD_TITLE"],
                        this.t(type)["NO_NEXT_EVENT_IN_LIMIT"]
                    );
                } else {
                    this.emit(':askWithCard',
                        this.t(type)["NO_NEXT_EVENT"] + ". " + this.t("ASK_CONTINUE"),
                        this.t("REPROMPT"),
                        this.t(type)["NEXT_EVENT_CARD_TITLE"],
                        this.t(type)["NO_NEXT_EVENT"]
                    );
                }
            } else {
                let fromDate = formatDate(response[0].from)
                let fromTime = formatTime(response[0].from)

                let result = "";
                //Change result string depending on if one ore multiple events have been returned
                if (response.length != 1) result = this.t("NEXT_EVENTS") + fromDate.toString() + "</say-as> um <say-as interpret-as='time'>" + fromTime.toString() + "</say-as>.";
                if (response.length == 1) result = this.t(type)["NEXT_EVENT_DATE"] + fromDate.toString() + "</say-as> um <say-as interpret-as='time'>" + fromTime.toString() + "</say-as>";

                //Iterate over all returned events
                response.forEach(element => {
                    //Set new session attributes
                    this.attributes["searchDate"] = element.from
                    this.attributes["excluded"].push(element.eventID)
                    this.attributes["continue"] = false

                    //Check if only one or multiple events have been found and change response accordingly
                    if (response.length == 1) {
                        result += ((this.attributes["location"]) ? (" " + this.t("IN_LOCATION") + " " + this.attributes["location"]) : "") + ". Zusammenfassung: <s>" + element.summary + "</s> " + this.t("ASK_CONTINUE");
                    } else {
                        result += this.t(type)["EVENT"] + ((this.attributes["location"]) ? (this.t("IN_LOCATION") + " " + this.attributes["location"]) : "") + ". Zusammenfassung: <s>" + element.summary + "</s> ";
                    }
                });

                //Add "Continue" prompt if multiple events have been found at the end, so it is returned only once
                if (response.length != 1) result += this.t("ASK_CONTINUE")

                this.emit(':askWithCard',
                    result,
                    this.t("REPROMPT"),
                    this.t(type)["NEXT_EVENT_CARD_TITLE"],
                    result
                );
            }
        }).catch((e) => {
            console.log(e)
            this.emit(':tellWithCard',
                this.t("DEFAULT_QUERY_ERROR"),
                this.t(type)["NEXT_EVENT_CARD_TITLE"],
                this.t("DEFAULT_QUERY_ERROR")
            );
        });
    },
    'Continue': function () {
        //Start "GetNextEvent" again, but with session attribute "Continue" set to true
        this.attributes["continue"] = true
        this.attributes["limit"] = false
        this.emitWithState("ExecuteGetNextEvent");
    },
    'AMAZON.CancelIntent': function () {
        //Reset session attributes
        this.attributes["dateTime"] = false
        this.attributes["location"] = false
        this.attributes["excluded"] = []
        this.attributes["continue"] = false
        this.attributes["limit"] = false
        this.handler.state = states.DEFAULTMODE
        this.emit(':ask', 'Abfrage beendet. Was möchtest du tun?');
    },
    'AMAZON.StopIntent': function () {
        this.emit(':tell', this.t('STOP_MESSAGE'));
    },
    'Unhandled': function () {
        this.emit(':ask', this.t("UNHANDLED_STATE"));
    },
})

//Events used when user wants to add an event. Will only be accessed when in "ADDEVENT" state
const addEventHandlers = Alexa.CreateStateHandler(states.ADDEVENTMODE, {
    'QuestionAnsweredWithEventType': function () {
        this.attributes["eventType"] = this.event.request.intent.slots.EventType.value;
        this.attributes["questionType"] = questionTypes.STARTDATE;
        this.emit(':ask', this.t("ASK_STARTDATE"));
    },
    'QuestionAnsweredWithText': function () {
        switch (this.attributes["questionType"]) {
            case questionTypes.SUMMARY:
                this.attributes[questionTypes.SUMMARY] = this.event.request.intent.slots.TEXTANSWER.value
                this.attributes["questionType"] = questionTypes.DESCRIPTION
                this.emit(':ask', this.t("ASK_DESCRIPTION"));
                break;
            case questionTypes.DESCRIPTION:
                this.attributes[questionTypes.DESCRIPTION] = this.event.request.intent.slots.TEXTANSWER.value
                this.emit(':ask', "Yo Motherfucker!");
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
                    this.attributes[questionTypes.STARTDATE] = extractDateRange(this.event.request.intent.slots)[0]
                    this.attributes["questionType"] = questionTypes.STARTTIME;
                    this.emit(':ask', this.t("ASK_TIME"));
                    break;
                case questionTypes.ENDDATE:
                    const value = extractDateRange(this.event.request.intent.slots)[0]
                    if (checkOverlappingDates(moment(this.attributes[questionTypes.STARTDATE]), value)) {
                        this.emit(':ask', this.t("ERROR_QUESTION_WITH_DATE_TIME_OVERLAP"));
                    } else {
                        this.attributes[questionTypes.ENDDATE] = value
                        this.emit(':ask', this.t("ASK_TIME"));
                    }
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
            let date
            let time
            switch (this.attributes["questionType"]) {
                case questionTypes.STARTDATE:
                    date = extractDateRange(this.event.request.intent.slots)[0]
                    time = extractTime(this.event.request.intent.slots)
                    date.set({
                        'hour': time.get('hour'),
                        'minute': time.get('minute'),
                        'second': time.get('second'),
                        'millisecond': time.get('millisecond')
                    })
                    this.attributes[questionTypes.STARTDATE] = date
                    if (this.attributes["eventType"] != "deadline") {
                        this.attributes["questionType"] = questionTypes.ENDDATE;
                        this.emit(':ask', this.t("ASK_ENDDATE"));
                    } else {
                        this.attributes["questionType"] = questionTypes.SUMMARY;
                        this.emit(':ask', this.t("ASK_SUMMARY"));
                    }
                    break;
                case questionTypes.ENDDATE:
                    date = extractDateRange(this.event.request.intent.slots)[0]
                    time = extractTime(this.event.request.intent.slots)
                    date.set({
                        'hour': time.get('hour'),
                        'minute': time.get('minute'),
                        'second': time.get('second'),
                        'millisecond': time.get('millisecond')
                    })
                    if (checkOverlappingDates(date, moment(this.attributes[questionTypes.STARTDATE]))) {
                        this.emit(':ask', this.t("ERROR_QUESTION_WITH_DATE_TIME_OVERLAP"));
                    } else {
                        this.attributes[questionTypes.ENDDATE] = date
                    }
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
            let time
            let date
            switch (this.attributes["questionType"]) {
                case questionTypes.STARTDATE:
                    this.attributes[questionTypes.STARTDATE] = extractTime(this.event.request.intent.slots)
                    if (this.attributes["eventType"] != "deadline") {
                        this.attributes["questionType"] = questionTypes.ENDDATE;
                        this.emit(':ask', this.t("ASK_ENDDATE"));
                    } else {
                        this.attributes["questionType"] = questionTypes.SUMMARY;
                        this.emit(':ask', this.t("ASK_SUMMARY"));
                    }
                    break;
                case questionTypes.ENDDATE:
                    time = extractTime(this.event.request.intent.slots)
                    if (checkOverlappingDates(time, moment(this.attributes[questionTypes.STARTDATE]))) {
                        this.emit(':ask', this.t("ERROR_QUESTION_WITH_DATE_TIME_OVERLAP"));
                    } else {
                        this.attributes[questionTypes.ENDDATE] = time
                        this.attributes["questionType"] = questionTypes.SUMMARY;
                        this.emit(':ask', this.t("ASK_SUMMARY"));
                    }
                    break;
                case questionTypes.STARTTIME:
                    date = moment(this.attributes[questionTypes.STARTDATE])
                    time = extractTime(this.event.request.intent.slots)
                    date.set({
                        'hour': time.get('hour'),
                        'minute': time.get('minute'),
                        'second': time.get('second'),
                        'millisecond': time.get('millisecond')
                    })
                    this.attributes[questionTypes.STARTDATE] = date

                    if (this.attributes["eventType"] != "deadline") {
                        this.attributes["questionType"] = questionTypes.ENDDATE;
                        this.emit(':ask', this.t("ASK_ENDDATE"));
                    } else {
                        this.attributes["questionType"] = questionTypes.SUMMARY;
                        this.emit(':ask', this.t("ASK_SUMMARY"));
                    }
                    break;
                case questionTypes.ENDTIME:
                    date = moment(this.attributes[questionTypes.ENDDATE])
                    time = extractTime(this.event.request.intent.slots)
                    date.set({
                        'hour': time.get('hour'),
                        'minute': time.get('minute'),
                        'second': time.get('second'),
                        'millisecond': time.get('millisecond')
                    })
                    this.attributes[questionTypes.ENDDATE] = date
                    this.attributes["questionType"] = questionTypes.SUMMARY;
                    this.emit(':ask', this.t("ASK_SUMMARY"));
                    break;
                default:
                    this.emit(':ask', this.t("ERROR_QUESTION_WITH_TIME"));
                    break;
            }
        }
    },
    'AMAZON.CancelIntent': function () {
        this.handler.state = ''
        delete this.attributes['STATE'];
        this.emit('LaunchRequest');
    },
    'AMAZON.StopIntent': function () {
        this.emit(':tell', this.t('STOP_MESSAGE'));
    },
    'Unhandled': function () {
        this.emit(':ask', this.t("ERROR_GENERAL"));
    }
});

function checkOverlappingDates(startDate, endDate) {
    return startDate.isAfter(endDate) || startDate.isSame(endDate, 'millisecond');
}

function formatDate(date) {
    return moment(date).format('YYYYMMDD');
}

function extractDateRange(slots) {
    let result = []

    if (slots && slots.Date && slots.Date.value) {
        try {
            let dates = new AmazonDateParser(slots.Date.value)
            result[0] = moment(dates["startDate"])
            result[1] = moment(dates["endDate"])
        } catch (e) {
            result[0] = moment().startOf('day');
            result[1] = moment().endOf('day');
        }
    } else {
        result[0] = moment().startOf('day');
        result[1] = moment().endOf('day');
    }

    return result
}

function extractQuery(slots) {
    if (slots && slots.Query && slots.Query.value) {
        return slots.Query.value
    } else {
        return ""
    }
}

function extractTime(slots) {
    if (slots && slots.Time && slots.Time.value) {
        switch (slots.Time.value) {
            case "EV":
                return moment("18:00", "HH:mm")
                break
            case "NI":
                return moment("24:00", "HH:mm")
                break
            case "AF":
                return moment("12:00", "HH:mm")
                break
            case "MO":
                return moment("06:00", "HH:mm")
                break
            default:
                return moment(slots.Time.value, "HH:mm")
                break;
        }
    } else {
        return null
    }
}

function extractType(slots) {
    if (slots &&
        slots.Type &&
        slots.Type.value &&
        slots.Type.resolutions &&
        slots.Type.resolutions.resolutionsPerAuthority &&
        slots.Type.resolutions.resolutionsPerAuthority[0] &&
        slots.Type.resolutions.resolutionsPerAuthority[0].status.code === "ER_SUCCESS_MATCH" &&
        slots.Type.resolutions.resolutionsPerAuthority[0].values &&
        slots.Type.resolutions.resolutionsPerAuthority[0].values[0] &&
        slots.Type.resolutions.resolutionsPerAuthority[0].values[0].value &&
        slots.Type.resolutions.resolutionsPerAuthority[0].values[0].value.name &&
        validEventType(slots.Type.resolutions.resolutionsPerAuthority[0].values[0].value.name)) {
        return slots.Type.resolutions.resolutionsPerAuthority[0].values[0].value.name
    } else {
        return "Event"
    }
}

function formatTime(time) {
    return moment(time).format('HH:mm');
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

exports.handler = function (event, context) {
    const alexa = Alexa.handler(event, context);
    if ('undefined' === typeof process.env.DEBUG) {
        alexa.APP_ID = APP_ID;
    }
    alexa.resources = languageStrings;
    alexa.registerHandlers(newSessionHandlers, defaultHandlers, addEventHandlers, nextEventHandlers);
    alexa.execute();
};