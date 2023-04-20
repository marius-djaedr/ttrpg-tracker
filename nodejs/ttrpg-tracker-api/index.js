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

server.use(
    function crossOrigin(req,res,next){
      res.header("Access-Control-Allow-Origin", "*");
      res.header("Access-Control-Allow-Methods", "*");
      res.header("Access-Control-Allow-Headers", "Content-Type");
      return next();
    }
  );

server.on('MethodNotAllowed', (req, res) => {
    if (req.method.toLowerCase() === 'options') {
        var allowHeaders = ['Accept', 'Accept-Version', 'Content-Type', 'Api-Version', 'Origin', 'X-Requested-With']; // added Origin & X-Requested-With

        if (res.methods.indexOf('OPTIONS') === -1) res.methods.push('OPTIONS');

        res.header('Access-Control-Allow-Credentials', true);
        res.header('Access-Control-Allow-Headers', allowHeaders.join(', '));
        res.header('Access-Control-Allow-Methods', res.methods.join(', '));
        res.header('Access-Control-Allow-Origin', req.headers.origin);

        return res.send(204);
    }
    else
        return res.send(new restify.MethodNotAllowedError());
  })


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
