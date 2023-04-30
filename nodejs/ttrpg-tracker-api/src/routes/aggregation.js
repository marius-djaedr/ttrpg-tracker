const ObjectId = require('mongodb').ObjectId;
const aggMain = require('../aggregation/aggMain');
const logger = require('../logger');

module.exports = function(ctx) {
    // extract context from passed in object
    const client = ctx.client;
    const server = ctx.server

    async function mongoWrapper(mongoFunc){
        try{
            const collection = client.db('TtrpgTracker').collection('Aggregation');
            return mongoFunc(collection);
        }finally{
            await client.close();
        }
    }
    
    server.get('/api/aggregation/mapping', (req, res, next) => {
        mongoWrapper(collection => collection.findOne({type:'MAPPING'}))
            .then(docs => {
                res.send(200, docs);
                next();
            })
            .catch(err => {
                res.send(500, err);
                next();
            });
    })
    
    server.get('/api/aggregation/:id', (req, res, next) => {
        const rawId = req.params.id
        if(ObjectId.isValid(rawId)){
            mongoWrapper(collection => collection.findOne({_id: new ObjectId(rawId),type:'AGGREGATION'}))
            .then(docs => {
                res.send(200, docs);
                next();
            })
            .catch(err => {
                res.send(500, err);
                next();
            });
        }else{
            res.send(500,'Invalid ID ['+rawId+']');
            next();
        }
    })

    server.post('/api/aggregation/run', (req, res, next) => {
        aggMain.runAggregation(client)
            .then((afterSend) => {
                res.send(200);
                next();
                afterSend();
            })
            .catch(err => {
                logger.error(err);
                res.send(500,`can't figure out how to send the error message, just check the logs`);
                next();
            });
    })
}