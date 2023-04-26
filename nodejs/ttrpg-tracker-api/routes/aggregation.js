const ObjectId = require('mongodb').ObjectId;
const aggMain = require('../aggregation/aggMain');

module.exports = function(ctx) {
    // extract context from passed in object
    const client = ctx.client;
    const server = ctx.server

    // assign collection to variable for further use
    const collection = client.db('TtrpgTracker').collection('Aggregation');
    const aggregator = new aggMain({ collection, client });

    
    server.get('/api/aggregation/mapping', (req, res, next) => {
        collection.findOne({type:'MAPPING'})
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
            collection.findOne({_id: new ObjectId(rawId),type:'AGGREGATION'})
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
        aggregator.runAggregation()
            .then(() => {
                res.send(200);
                next();
            })
            .catch(err => {
                console.log('FAILED AGGREGATION ' + err);
                res.send(500, err);
                next();
            });
    })
}