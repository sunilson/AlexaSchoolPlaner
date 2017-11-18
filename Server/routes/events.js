var express = require('express');
var router = express.Router();
var eventService = require("../services/eventService");
var ObjectOperations = require("../utils/objectOperations");

//Route for creating a new event
router.post('/new', function (req, res, next) {
    //Parse Event from request and trim it
    var event = ObjectOperations.parseObject(req.body.data);

    //Get user from authentication
    var user = req.user;

    //Save event to database and react to result
    eventService.saveEvent(event, /*user.id*/ "blub").then((result) => {
        res.sendStatus(201);
    }).catch((err) => {
        return next(err);
    });
});

module.exports = router;