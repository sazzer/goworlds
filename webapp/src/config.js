/**
 * Get the configuration setting with the given name
 * @param name the name of the setting
 * @return the setting
 */
function getConfig(name) {
    return (window.GOWORLDS_CONFIG || {})[name];
}

module.exports = {
    getConfig: getConfig
};
