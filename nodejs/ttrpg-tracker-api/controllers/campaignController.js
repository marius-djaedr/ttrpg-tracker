const Joi = require('@hapi/joi');
Joi.objectId = require('joi-objectid')(Joi)

exports.plugin = {
    name: 'campaignController',
    version: '1.0.0',
    register: async function (server, options) {

        //get all entities
        server.route({
            method: 'GET',
            path: '/campaigns',
            handler: async (req, h) => {
                const campaigns = await req.mongo.db.collection('TtrpgTracker').find({Type:'CAMPAIGN'}).toArray();
                return campaigns;
            }
        });

        // Get a single entity
        server.route({
            method: 'GET',
            path: '/campaigns/{id}',
            options: {
                validate: {
                    params: Joi.object({
                        id: Joi.objectId()
                    })
                }
            },
            handler: async (req, h) => {
                const id = req.params.id
                const ObjectID = req.mongo.ObjectID;
                const campaign = await req.mongo.db.collection('TtrpgTracker').findOne({_id: new ObjectID(id)});
                return campaign;
            }
        });

        // Add a new entity to the database
        server.route({
            method: 'POST',
            path: '/campaigns',
            handler: async (req, h) => {
                const payload = req.payload
                const status = await req.mongo.db.collection('TtrpgTracker').insertOne(payload);
                return status;
            }
        });

        // Update the details of an entity
        server.route({
            method: 'PUT',
            path: '/campaigns/{id}',
            options: {
                validate: {
                    params: Joi.object({
                        id: Joi.objectId()
                    })
                }
            },
            handler: async (req, h) => {
                const id = req.params.id
                const ObjectID = req.mongo.ObjectID;
                const payload = req.payload
                const status = await req.mongo.db.collection('Books').updateOne({_id: ObjectID(id)}, {$set: payload});
                return status;
            }
        });

        // Delete an entity from the database
        server.route({
            method: 'DELETE',
            path: '/campaigns/{id}',
            options: {
                validate: {
                    params: Joi.object({
                        id: Joi.objectId()
                    })
                }
            },
            handler: async (req, h) => {
                const id = req.params.id
                const ObjectID = req.mongo.ObjectID;
                const status = await req.mongo.db.collection('Books').deleteOne({_id: ObjectID(id)});
                return status;
            }
        });
}
};