var objectOperations = module.exports = {};

function trimObject(object) {
    for (var key in object) {
        if (object[key] === 'string') {
            object[key] = object[key].trim();
        }
    }
    return object;
}

objectOperations.parseObject = (object) => {
    return trimObject(JSON.parse(object));
}