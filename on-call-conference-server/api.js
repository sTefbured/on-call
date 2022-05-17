const axios = require("axios");

const SERVER_LINK = "https://raw.githubusercontent.com/pradt2/always-online-stun/master/valid_hosts.txt";

const api = {
    getStunServers() {
        return axios.get(SERVER_LINK, {withCredentials: false})
            .then(response => response.data.split('\n'));
    }
}

module.exports = api;