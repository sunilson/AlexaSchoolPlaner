var objectOperations = module.exports = {};

function trimObject(object) {
    for (var key in object) {
        if (object[key] === 'string') {
            object[key] = object[key].trim();
        }
    }
    return object;
}

objectOperations.trimObject = (object) => {
    return trimObject(object);
}

objectOperations.parseObject = (object) => {
    if (object) {
        return trimObject(JSON.parse(object));
    }

    return null;
}