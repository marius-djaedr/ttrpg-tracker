const Hapi = require('@hapi/hapi');
const Path = require('path');
const Hoek = require('@hapi/hoek');

const init = async () => {

    const server = Hapi.server({
        port: 3000,
        host: 'localhost'
    });    

    await server.register(require('@hapi/vision'));
    server.views({
        engines: {
            hbs: require('handlebars')
        },
        relativeTo: __dirname,
        path: 'templates',
        layout: true,
        layoutPath: 'templates/layout'
    });

    await server.register([require('./controllers/campaignController'), require('./controllers/characterController'), require('./controllers/sessionController')]);
    
    server.route({
        method: '*',
        path: '/{any*}',
        handler: function (request, h) {

            return '404 Error! Page Not Found!';
        }
    });

    await server.start();
    console.log('Server running on %s', server.info.uri);
}

init();