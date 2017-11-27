var express = require('express');
var router = express.Router();
var ObjectOperations = require("../utils/objectOperations");
var UserModel = require("../data/models/userModel");

//Route for creating a new user
router.post('/register', function (req, res, next) {

    //Parse User from request and trim it
    var data = ObjectOperations.parseObject(req.body.data);

    //PLaceholder operation
    new UserModel(data).save().then(() => {
        res.sendStatus(201);
    });
});

module.exports = router;