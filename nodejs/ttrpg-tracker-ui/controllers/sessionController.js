exports.plugin = {
    name: 'sessionController',
    version: '1.0.0',
    register: async function (server, options) {

        server.route({
            method: 'GET',
            path: '/sessions',            
            handler: async (req, h) => {
                return 'test passed sessions';
            }
        });

        // etc...
        //await someAsyncMethods();
    }
};