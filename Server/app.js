const express = require('express');
const path = require('path');
const logger = require('morgan');
const cookieParser = require('cookie-parser');
const bodyParser = require('body-parser');
const events = require('./routes/event-route');
const auth = require('./routes/auth-route');
const cors = require("cors");
const mongoose = require("mongoose");
const cfg = require("./config.js");
const passport = require('passport');
const strategy = require('./strategies/local-strategy.js');
const app = express();

//Db setup
mongoose.connect(cfg.mongoDBURL, {
  useMongoClient: true
});

//Set promise library
mongoose.Promise = global.Promise;

//Allow cross origin 
app.use(cors());
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: false
}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

//Setup authentication
passport.use(strategy);
app.use(passport.initialize());

//Only allow access to the events route when authorized
app.use('/events', passport.authenticate('jwt', cfg.jwtSession), events);
//Auth route does not need authorization
app.use('/auth', auth);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // console.log(err);

  // render the error page
  console.log(err);
  res.status(err.status || 500).send(err.message);
});

module.exports = app;