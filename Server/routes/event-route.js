var express = require('express');
var router = express.Router();
var eventService = require("../services/eventService");
var ObjectOperations = require("../utils/objectOperations");
var eventModel = require("../data/models/eventModel");
var userModel = require("../data/models/userModel");
var moment = require("moment");

//Route for creating a new event
router.post('/new', (req, res, next) => {

    //Parse Event from request and trim it
    var event = ObjectOperations.trimObject(req.body);

    //Get user from authentication
    var user = req.user;

    //Save event to database and react to result
    eventService.saveEvent(event, /*user.id*/ "5a103e699c04124d2813693a").then((result) => {
        res.status(201).json(result);
    }).catch((err) => {
        return next(err);
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

    if (!from || !to || !Number.isInteger(from) || !Number.isInteger(to)) {
        next({
            status: 400,
            message: "Invalid date range"
        });
    }

    eventModel.find({
        author: "5a103e699c04124d2813693a" /* user.id */ ,
        from: {
            $lt: new Date(to)
        },
        to: {
            $gte: new Date(from)
        }
    }).lean().exec().then((result) => {
        console.log(result);
        res.status(200).json(result);
    }).catch((error) => {
        next(error);
    });
});

module.exports = router;