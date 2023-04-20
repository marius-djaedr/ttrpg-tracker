const restify = require('restify');
const PropertiesReader = require('properties-reader');
const prop = PropertiesReader('./mongodb.properties');
const mongodb = require('mongodb').MongoClient;

const server = restify.createServer({
    name    : 'ttrpg-tracker-api',
    version : '1.0.0'
})

server.use(restify.plugins.jsonBodyParser({ mapParams: true }))
server.use(restify.plugins.acceptParser(server.acceptable))
server.use(restify.plugins.queryParser({ mapParams: true }))
server.use(restify.plugins.fullResponse())

console.log('setup start')
// establish connection to mongodb atlas
mongodb.connect(prop.get('mongodb.url'))
    .then(client => {
        console.log('mongo done')
        require('./routes')({ client, server })
    
        server.listen(3300, function() {
            console.log('%s listening at %s', server.name, server.url);
        })
    })
    .catch(err =>{
        console.log('An error occurred while attempting to connect to MongoDB', err)
        process.exit(1)
    })
