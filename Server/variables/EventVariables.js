module.exports = {
    description: {
        maxLength: 20,
        minLength: 5
    },
    types: {
        schoolAppointment: 1,
        specialAppointment: 2,
        deadline: 3
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