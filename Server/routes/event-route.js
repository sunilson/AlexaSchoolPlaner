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

    if (!url || !isUrl(url)) {
        return next({
            status: 400,
            message: "Invalid URL"
        });
    }

    const userDoc = userModel.findById(user._id);

    if (userDoc.icalurl === url) {
        return res.sendStatus(200);
    }

    //Add url to user object
    userDoc.update({
        icalurl: url
    }).then(() => {
        //Execute import for new URL now
        return eventService.executeImport(url, user._id, algoliaEventIndex);
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

router.delete('/removeImport', (req, res, next) => {
    const user = req.user;
});

router.get('/executeSingleImport', (req, res, next) => {

});

router.get('/executeImports', (req, res, next) => {

    const token = req.query.token;

    if (token !== cfg.importToken) {
        return res.sendStatus(401);
    }

    //Get all users with a iCal URL set
    userModel.find({
        icalurl: {
            $ne: null
        }
    }).lean().exec().then(result => {
        for (let i = 0; i < result.length; i++) {
            eventService.executeImport(result[i].icalurl, result[i]._id, algoliaEventIndex);
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

//Route for searching the index for the next event at a certain location
router.get('/searchNextEventAtLocation', (req, res, next) => {

    const query = {};

    if (req.query.query) {
        query.query = req.query.query;
    }

    let date;
    (req.query.date) ? (date = req.query.date) : (date = Date.now());
    query.filters = "from > " + date;

    if (req.query.type) {
        query.filters += "AND type = " + req.query.type;
    }

    query.hitsPerPage = 1;

    algoliaEventIndex.search({
            query,
            restrictSearchableAttributes: [
                'location'
            ]
        },
        (err, content) => {
            if (err) return next(err);
            res.status(200).json(content.hits[0]);
        });
});

//Get the event nearest to a specific date
router.get('/nearestEvent', (req, res, next) => {
    let date;
    (req.query.date) ? date = parseInt(req.query.date): date = Date.now();

    const user = req.user;
    const query = {
        author: user._id,
        from: {
            $gte: new Date(date)
        }
    }

    if (req.query.type) {
        query["type"] = req.query.type
    }

    eventModel.findOne(query).sort({
        "from": 1
    }).lean().exec().then((result) => {
        res.status(200).json(result);
    }).catch((error) => {
        next(error);
    });
});

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
    }).catch((e) => {
        next(e);
    });
});

router.get('/', (req, res, next) => {
    let from = parseInt(req.query.from);
    let to = parseInt(req.query.to);
    const type = parseInt(req.query.type);
    const user = req.user;

    if (!from || !Number.isInteger(from)) {
        from = moment().startOf('day').valueOf();
    }

    if (!to || !Number.isInteger(to)) {
        to = moment(from).add(1, 'days').valueOf();
    }

    var searchOptions = {
        author: user._id,
        from: {
            $lt: new Date(to)
        },
        to: {
            $gte: new Date(from)
        }
    }

    if (type) {
        searchOptions["type"] = type;
    }

    eventModel.find(searchOptions).lean().exec().then((result) => {
        res.status(200).json(result);
    }).catch((error) => {
        next(error);
    });
});

module.exports = router;