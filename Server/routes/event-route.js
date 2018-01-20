var express = require('express');
var router = express.Router();
var eventService = require("../services/eventService");
var ObjectOperations = require("../utils/objectOperations");
var eventModel = require("../data/models/eventModel");
var userModel = require("../data/models/userModel");
var moment = require("moment");
var config = require("../config");
var algoliasearch = require("algoliasearch");
var algoliaClient = algoliasearch(config.algolia.appId, config.algolia.apiKey);
var algoliaEventIndex = algoliaClient.initIndex('bachelor_thesis_events');
algoliaEventIndex.setSettings(config.algoliaIndexSettings);

//Route for creating a new event
router.post('/new', (req, res, next) => {

    //Parse Event from request and trim it
    var event = ObjectOperations.trimObject(req.body);

    //Get user from authentication
    var user = req.user;

    //Save event to database and react to result
    eventService.saveEvent(event, /*user.id*/ "5a103e699c04124d2813693a").then((result) => {

        //Add event to algolia search index
        let searchIndex = {};
        searchIndex["description"] = result.description;
        searchIndex["summary"] = result.summary;
        searchIndex["eventId"] = result.id;
        searchIndex["type"] = result.type;
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
    let query = {};

    query.query = req.query.query;
    query.filters = "type = " + req.query.type + " AND from > " + Date.now();
    query.hitsPerPage = 1;

    algoliaEventIndex.search(query, (err, content) => {
        if (err) return next(err);
        res.status(200).json(content.hits[0]);
    });

});


router.get('/nearestEvent', (req, res, next) => {
    let date = parseInt(req.query.date);

    eventModel.findOne({
        author: "5a103e699c04124d2813693a" /* user.id */ ,
        from: {
            $gte: new Date(date)
        },
        to: {
            $lt: new Date(date + 10800000)
        }
    }).lean().exec().then((result) => {
        res.status(200).json(result);
    }).catch((error) => {
        next(error);
    });
});

router.get('/:id', (req, res, next) => {

    //Get user from authentication
    var user = req.user;

    eventModel.findOne({
        _id: req.params.id,
        author: "5a103e699c04124d2813693a" /* user.id */
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
    var from = parseInt(req.query.from);
    var to = parseInt(req.query.to);
    var type = parseInt(req.query.type);

    if (!from || !to || !Number.isInteger(from) || !Number.isInteger(to)) {
        next({
            status: 400,
            message: "Invalid date range"
        });
    }

    var searchOptions = {
        author: "5a103e699c04124d2813693a" /* user.id */ ,
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
        console.log(result);
        res.status(200).json(result);
    }).catch((error) => {
        next(error);
    });
});

module.exports = router;