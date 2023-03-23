const Boom = require('boom');

exports.plugin = {
    name: 'crudController',
    version: '1.0.0',
    register: async function (server, options) {

        const typeMap = {'campaigns':'CAMPAIGN','characters':'CHARACTER','sessions':'SESSION'};
        function convertType(rawType){
            if(!Object.keys(typeMap).includes(rawType)){
                throw Boom.notFound('Invalid type ['+rawType+']');
            }
            return typeMap[rawType];
        }

        //get all entities
        server.route({
            method: 'GET',
            path: '/api/{type}',
            handler: async (req, h) => {
                const rawType = req.params.type;
                const campaigns = await req.mongo.db.collection('TtrpgTracker').find({Type:convertType(rawType)}).toArray();
                return campaigns;
            }
        });

        // Get a single entity
        server.route({
            method: 'GET',
            path: '/api/{type}/{id}',
            handler: async (req, h) => {
                const rawType = req.params.type;
                const id = req.params.id
                const ObjectID = req.mongo.ObjectID;
                const campaign = await req.mongo.db.collection('TtrpgTracker').findOne({_id: new ObjectID(id),Type:convertType(rawType)});
                return campaign;
            }
        });

        //get all child entities for parent
        server.route({
            method: 'GET',
            path: '/api/{parentType}/{id}/{childType}',
            handler: async (req, h) => {
                const rawType = req.params.childType;
                const id = req.params.id
                const ObjectID = req.mongo.ObjectID;
                const campaigns = await req.mongo.db.collection('TtrpgTracker').find({Type:convertType(rawType),ParentId: new ObjectID(id)}).toArray();
                return campaigns;
            }
        });

        // Add a new entity to the database
        server.route({
            method: 'POST',
            path: '/api/{type}',
            handler: async (req, h) => {
                const rawType = req.params.type;
                const payload = req.payload
                payload.Type = convertType(rawType);
                const status = await req.mongo.db.collection('TtrpgTracker').insertOne(payload);
                return status;
            }
        });

        // Update the details of an entity
        server.route({
            method: 'PUT',
            path: '/api/{type}/{id}',
            handler: async (req, h) => {
                const rawType = req.params.type;
                const id = req.params.id
                const ObjectID = req.mongo.ObjectID;
                const payload = req.payload
                const status = await req.mongo.db.collection('TtrpgTracker').updateOne({_id: ObjectID(id),Type:convertType(rawType)}, {$set: payload});
                return status;
            }
        });

        // Delete an entity from the database
        server.route({
            method: 'DELETE',
            path: '/api/{type}/{id}',
            handler: async (req, h) => {
                const rawType = req.params.type;
                const id = req.params.id
                const ObjectID = req.mongo.ObjectID;
                const status = await req.mongo.db.collection('TtrpgTracker').deleteOne({_id: ObjectID(id),Type:convertType(rawType)});
                return status;
            }
        });
    }
};