exports.plugin = {
    name: 'characterController',
    version: '1.0.0',
    register: async function (server, options) {

        server.route({
            method: 'GET',
            path: '/characters',
            handler: function (request, h) {
                return 'test passed characters';
            }
        });

        // etc...
        //await someAsyncMethods();
    }
};