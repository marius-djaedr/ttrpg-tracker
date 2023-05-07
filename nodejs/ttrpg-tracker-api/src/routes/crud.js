const ObjectId = require('mongodb').ObjectId;

module.exports = function(ctx) {
    // extract context from passed in object
    const client = ctx.client;
    const server = ctx.server
    
    const collection = client.db('TtrpgTracker').collection('TtrpgTracker');
    //type conversion function
    const typeMap = {'frameworks':'FRAMEWORK','systems':'SYSTEM','campaigns':'CAMPAIGN','characters':'CHARACTER','sessions':'SESSION'};
    function convertType(rawType, res, next){
        if(Object.keys(typeMap).includes(rawType)){
            return typeMap[rawType];
        }else{
            res.send(404,'Invalid type ['+rawType+']');
            next();
            return null;
        }
    }

    function convertId(rawId, res, next){
        if(ObjectId.isValid(rawId)){
            return new ObjectId(rawId);
        }else{
            res.send(500,'Invalid ID ['+rawId+']');
            next();
            return null;
        }
    }

    //get all entities
    server.get('/api/:type', (req, res, next) => {
        const type = convertType(req.params.type, res, next);
        if(!type)return;

        collection.find({type:type}).toArray()
            .then(docs => {
                res.send(200, docs);
                next();
            })
            .catch(err => {
                res.send(500, err)
                next();
            });
    })

    //get one entity
    server.get('/api/:type/:id', (req, res, next) => {
        const type = convertType(req.params.type, res, next);
        if(!type)return;
        const id = convertId(req.params.id, res, next);
        if(!id)return;
        collection.findOne({_id: id,type:type})
            .then(docs => {
                res.send(200, docs);
                next();
            })
            .catch(err => {
                res.send(500, err);
                next();
            });
    })

    //get all child entities for parent
    server.get('/api/:parentType/:id/:childType', (req, res, next) => {
        const parentType = convertType(req.params.parentType, res, next);
        if(!parentType)return;
        const childType = convertType(req.params.childType, res, next);
        if(!childType)return;
        const id = convertId(req.params.id, res, next);
        if(!id)return;
        collection.find({type:childType,parentId: id}).toArray()
            .then(docs => {
                res.send(200, docs);
                next();
            })
            .catch(err => {
                res.send(500, err);
                next();
            });
    })
    
    // Add a new entity to the database
    server.post('/api/:type', (req, res, next) => {
        const type = convertType(req.params.type, res, next);
        if(!type)return;

        const data = Object.assign({}, req.body, {
            type: type
        });
        delete data._id

        collection.insertOne(data)
            .then(doc => {
                res.send(200, {_id:doc.insertedId});
                next();
            })
            .catch(err => {
                res.send(500, err);
                next();
            });
    })

    //Update an entity
    server.put('/api/:type/:id', (req, res, next) => {
        const type = convertType(req.params.type, res, next);
        if(!type)return;
        const id = convertId(req.params.id, res, next);
        if(!id)return;

        const data = req.body;
        delete data._id;
        delete data.Type;
        
        // find and update document based on passed in id (via route)
        collection.updateOne({_id: id, type: type}, { $set: data })
            .then(doc => {
                res.send(200);
                next();
            })
            .catch(err => {
                res.send(500, err);
                next();
            });
    })

    // Delete an entity from the database
    server.del('/api/:type/:id', (req, res, next) => {
        const type = convertType(req.params.type, res, next);
        if(!type)return;
        const id = convertId(req.params.id, res, next);
        if(!id)return;

        // remove one document based on passed in id (via route)
        collection.deleteOne({ _id: id,type: type })
            .then(doc => {
                res.send(200);
                next();
            })
            .catch(err => {
                res.send(500, err);
                next();
            });
    })
    
}