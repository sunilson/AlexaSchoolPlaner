module.exports = {
    description: {
        maxLength: 20,
        minLength: 5
    },
    types: {
        schoolAppointment: 0,
        specialAppointment: 1,
        deadline: 2
    },
    summary: {
        maxLength: 120,
        minLength: 1
    },
    location: {
        maxLength: 120,
        minLength: 1
    },
    dates: {
        deadlineOffset: 3600000
    }
}