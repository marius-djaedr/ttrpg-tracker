const Hapi = require('@hapi/hapi');
const Path = require('path');
const Hoek = require('@hapi/hoek');

const init = async () => {

    const server = Hapi.server({
        port: 3300,
        host: 'localhost'
    });   
    server.realm.modifiers.route.prefix = '/api';

    await server.register({
        plugin: require('hapi-mongodb'),
        options: {
          url: 'MONGO URL',
          settings: {
              useUnifiedTopology: true
          },
          decorate: true
        }
    });

    await server.register([require('./controllers/campaignController'), require('./controllers/characterController'), require('./controllers/sessionController')]);

    await server.start();
    console.log('Server running on %s', server.info.uri);
}

init();