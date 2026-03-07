const logger = require('./src/logger');
const PropertiesReader = require('properties-reader');
const prop = PropertiesReader('./mongodb.properties');
const mongodb = require('mongodb').MongoClient;
const aggMain = require('./src/aggregation/aggMain');


logger.info('setup start')
// establish connection to mongodb atlas
mongodb.connect(prop.get('mongodb.url'))
    .then(client => {
        logger.info('mongo done')
        const aggCollection = client.db('TtrpgTracker').collection('Aggregation');
        const dataCollection = client.db('TtrpgTracker').collection('TtrpgTracker');
        
        aggMain.runAggregation(aggCollection, dataCollection)
            .then((afterSend) => {
                logger.info("calculation done, start drive export")
                afterSend().then(ret => {
                    logger.info("job's done")
                    process.exit(1)
                });
            })
            .catch(err => {
                logger.error(err);
                process.exit(1)
            });
    })
    .catch(err =>{
        logger.info('An error occurred while attempting to connect to MongoDB', err)
        process.exit(1)
    })