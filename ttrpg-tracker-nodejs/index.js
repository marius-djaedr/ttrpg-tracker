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

    await server.register({
        plugin: require('hapi-mongodb'),
        options: {
          url: 'mongodb+srv://mongo_app_user:KzhLwMB9Xe9kRaSV@cluster0.ur7ycf3.mongodb.net/TtrpgTracker?retryWrites=true&w=majority',
          settings: {
              useUnifiedTopology: true
          },
          decorate: true
        }
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