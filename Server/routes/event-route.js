const express = require('express');
const router = express.Router();
const eventService = require("../services/eventService");
const ObjectOperations = require("../utils/objectOperations");
const eventModel = require("../data/models/eventModel");
const userModel = require("../data/models/userModel");
const moment = require("moment");
const config = require("../config");
const algoliasearch = require("algoliasearch");
const algoliaClient = algoliasearch(config.algolia.appId, config.algolia.apiKey);
const algoliaEventIndex = algoliaClient.initIndex('bachelor_thesis_events');
const isUrl = require("is-url");
const cfg = require("../config.js");
algoliaEventIndex.setSettings(config.algoliaIndexSettings);

router.post('/import', (req, res, next) => {
    const url = req.body.url;
    const user = req.user;
    const type = req.body.type;

    if (!url || !isUrl(url) || type == null || !Number.isInteger(type) || type < 0 || type > 2) {
        return next({
            status: 400,
            message: "Invalid URL or type"
        });
    }

    const userDoc = userModel.findById(user._id)

    if (userDoc.icalurl === url) return res.sendStatus(200)

    //Add url to user object
    userDoc.update({
        icalurl: url,
        icaltype: type
    }).then(() => {
        //Execute import for new URL now
        return eventService.executeImport(url, user._id, algoliaEventIndex, type);
    }).then(() => {
        res.sendStatus(200);
    }).catch(e => {
        userDoc.update({
            $unset: {
                icalurl: 1
            }
        });
        next(e);
    });
});

//Updates iCal imports for all users
router.get('/executeImports', (req, res, next) => {

    const token = req.query.token;

    //Check if authorized
    if (token !== cfg.importToken) {
        return res.sendStatus(401);
    }

    //Get all users with a iCal URL set
    userModel.find({
        icalurl: {
            $ne: null
        }
    }).lean().exec().then(result => {
        //Do import for evvery user
        for (let i = 0; i < result.length; i++) {
            eventService.executeImport(result[i].icalurl, result[i]._id, algoliaEventIndex, result.icaltype);
        }
        res.sendStatus(200);
    }).catch(e => {
        next(e);
    });
});

//Route for creating a new event
router.post('/new', (req, res, next) => {

    //Parse Event from request and trim it
    const event = ObjectOperations.trimObject(req.body);

    console.log(event)

    //Get user from authentication
    const user = req.user;

    //Save event to database and react to result
    eventService.saveEvent(event, user._id).then((result) => {

        //Add event to algolia search index
        let searchIndex = {};
        searchIndex["author"] = result.author;
        searchIndex["description"] = result.description;
        searchIndex["summary"] = result.summary;
        searchIndex["eventID"] = result.id;
        searchIndex["objectID"] = result.id;
        searchIndex["type"] = result.type;
        searchIndex["location"] = result.location;
        searchIndex["from"] = Date.parse(result.from);
        searchIndex["to"] = Date.parse(result.to);
        algoliaEventIndex.addObject(searchIndex);

        return res.status(201).json(result);
    }).catch((err) => {
        return next(err);
    });
});

router.put('/', (req, res, next) => {
    //Parse Event from request and trim it
    const event = ObjectOperations.trimObject(req.body);

    //Get user from authentication
    const user = req.user;

    eventModel.findById(event.id).exec().then(oldEvent => {
        oldEvent.summary = event.summary;
        oldEvent.description = event.description;
        oldEvent.from = new Date(event.from);
        oldEvent.to = new Date(event.to);
        oldEvent.location = event.location;
        return oldEvent.save();
    }).then((result) => {
        res.status(201).json(result);
    }).catch(e => {
        next(e);
    });
});

/*
//Search for an event in the future containing the search query term
router.get('/searchNextEvent', (req, res, next) => {
    const query = {};

    if (req.query.query) {
        query.query = req.query.query;
    }

    query.filters = "from > " + Date.now();

    if (req.query.type) {
        query.filters += "AND type = " + req.query.type;
    }

    query.hitsPerPage = 1;

    algoliaEventIndex.search(query, (err, content) => {
        if (err) return next(err);
        res.status(200).json(content.hits[0]);
    });
});
*/

//Get the event/events nearest to a specific date of type and location (type and location are optional)
router.get('/nextEvent', (req, res, next) => {
    const user = req.user;
    const query = {};

    let date;
    (req.query.date) ? date = parseInt(req.query.date): date = Date.now();

    //Check if request is limited, which means that only events inside of a one hour timeframe should be found
    if (req.query.limit === "true") {
        query.filters = "from >= " + date + " AND from <= " + (date + 3600000) + " AND author:" + user._id;
    } else {
        query.filters = "from >= " + date + " AND author:" + user._id;
    }

    //Exclude already found events
    if (req.query.excluded) {
        let excluded = JSON.parse(req.query.excluded)
        excluded.forEach(id => {
            query.filters += " AND NOT eventID:" + id
        });
    }

    //Filter for event type
    if (req.query.type) {
        query.filters += " AND type = " + req.query.type;
    }

    //Filter for specific location or general search term
    if (req.query.location) {
        query["query"] = req.query.location
        query["restrictSearchableAttributes"] = [
            'location'
        ]
    } else if (req.query.query) {
        query["query"] = req.query.query
        query["restrictSearchableAttributes"] = [
            'summary',
            'description'
        ]
    }

    //Limit to max 5, so we don't get the whole dataset after the from date. There shouldn't be more than 5 appointments at the exact same time
    query.hitsPerPage = 5;

    algoliaEventIndex.search(query,
        (err, content) => {
            if (err) return next(err);

            //Only return events that start at the exact same time
            const result = []
            for (let i = 0; i < content.hits.length; i++) {
                if (i == 0) result.push(content.hits[i])
                else {
                    if (content.hits[0].from == content.hits[i].from) result.push(content.hits[i]);
                    else i = content.hits.length;
                }
            }

            //Rename id 
            result.forEach(element => {
                element["id"] = element["eventID"];
                delete element["eventID"];
            })

            res.status(200).json(result);
        });
});

//Getting a single event by id
router.get('/:id', (req, res, next) => {

    //Get user from authentication
    const user = req.user;

    eventModel.findOne({
        _id: req.params.id,
        author: user._id
    }).lean().exec().then((event) => {
        if (event) {
            return res.status(200).json(event);
        }
        next({
            status: 400,
            message: "Nothing found!"
        });
    }).catch(e => next(e));
});

//Gettings multiple events in a specific time range of a specific type
router.get('/', (req, res, next) => {
    let from = parseInt(req.query.from)
    let to = parseInt(req.query.to)
    const type = parseInt(req.query.type)
    const user = req.user

    //Check if start and end values are set, if not, set them to today
    if (!from || !Number.isInteger(from)) from = moment().startOf('day').valueOf()
    if (!to || !Number.isInteger(to)) to = moment(from).add(1, 'days').valueOf()

    const searchOptions = {
        author: user._id,
        from: {
            $lt: new Date(to)
        },
        to: {
            $gte: new Date(from)
        }
    }

    //Limit to specific event type
    if (type) searchOptions["type"] = type

    eventModel.find(searchOptions).lean().exec().then(result => {
        res.status(200).json(result);
    }).catch(error => {
        next(error);
    });
});

module.exports = router