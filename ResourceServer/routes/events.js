var express = require('express');
var router = express.Router();
var eventService = require("../services/eventService");
var ObjectOperations = require("../utils/objectOperations");

router.get('/new', function (req, res, next) {
    //Parse Event from request and trim it
    var event = ObjectOperations.parseObject(req.body.data);
    //Get user from authentication
    var user = req.user;

    //Save event to database and react to result
    eventService.saveEvent(event, user.id).then((result) => {
        res.sendStatus(201);
    }).catch((err) => {
        return next(err);
    });
});

module.exports = router;