exports.plugin = {
    name: 'campaignController',
    version: '1.0.0',
    register: async function (server, options) {

        server.route({
            method: 'GET',
            path: '/campaigns',
            handler: async (req, h) => {
                const campaignList = await req.mongo.db.collection('TtrpgTracker').find({Type:'CAMPAIGN'}).toArray();
                return h.view('campaignView',{campaigns:campaignList});
            }
        });

        // etc...
        //await someAsyncMethods();
    }
};