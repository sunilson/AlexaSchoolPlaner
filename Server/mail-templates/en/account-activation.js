var template = function (recipient, token) {
    this.message = {
        from: 'Excited User <me@samples.mailgun.org>',
        to: recipient,
        subject: 'Hello ' + recipient,
        text: 'Your token: ' + token
    }
};

module.exports = template;