const request = require('request-promise');

exports.plugin = {
    name: 'campaignController',
    version: '1.0.0',
    register: async function (server, options) {

        server.route({
            method: 'GET',
            path: '/campaigns',
            handler: async (req, h) => {
                //TODO planning to move to async front end anyways, so this is "good enough" for now
                var options = {
                    uri: 'http://localhost:3300/api/campaigns',
                    method: 'GET',
                    json: true
                };
                var responseBody = await request(options);
                var campaignList = responseBody;
                return h.view('campaignView',{campaigns:campaignList});
            }
        });

        // etc...
        //await someAsyncMethods();
    }
};