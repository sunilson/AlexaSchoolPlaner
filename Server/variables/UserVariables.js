module.exports = {
    userName: {
        minLength: 5,
        maxLength: 15
    },
    password: {
        minLength: 6,
        maxLength: 100
    },
    type: {
        standard: "standard",
        google: "google"
    },
    auth: {
        accessExpire: "1000d",
        refreshExpire: "1000d"
    }
}