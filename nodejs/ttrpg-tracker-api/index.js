const Hapi = require('@hapi/hapi');
const Boom = require('boom');
const PropertiesReader = require('properties-reader');
const prop = PropertiesReader('./mongodb.properties');

const init = async () => {

    const server = Hapi.server({
        port: 3300,
        host: 'localhost',
        routes: { cors: true }
    });   

    await server.register({
        plugin: require('hapi-mongodb'),
        options: {
          url: prop.get('mongodb.url'),
          settings: {
              useUnifiedTopology: true
          },
          decorate: true
        }
    });

    await server.register(require('./controllers/crudController'));

    
    server.route({
        method: '*',
        path: '/{any*}',
        handler: function (request, h) {
            throw Boom.notFound();
        }
    });

    await server.start();
    console.log('Server running on %s', server.info.uri);
}

init();