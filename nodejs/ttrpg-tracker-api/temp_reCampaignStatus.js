const logger = require('./src/logger');
const PropertiesReader = require('properties-reader');
const prop = PropertiesReader('./mongodb.properties');
const mongodb = require('mongodb').MongoClient;


logger.info('setup start')
// establish connection to mongodb atlas
mongodb.connect(prop.get('mongodb.url'))
    .then(client => {
        logger.info('mongo done')
        const dataCollection = client.db('TtrpgTracker').collection('TtrpgTracker');

        queryAndUpdate(dataCollection).then(ret =>{
            logger.info("job's done")
            process.exit(1)
        })
    })
    .catch(err =>{
        logger.info('An error occurred while attempting to connect to MongoDB', err)
        process.exit(1)
    })

async function queryAndUpdate(dataCollection){
    const docs = await dataCollection.find({type:'CAMPAIGN'}).toArray()
    
    const allUpdatePromises = [];
    for(const record of docs){
        let completedOld = record.completed
        if(completedOld === '' || completedOld === null || completedOld === 'ongoing'){
            record.completed = 'ongoing'
        }else if(completedOld === 'true' || completedOld === true || completedOld === 'completed'){
            record.completed = 'completed'
        }else if(completedOld === 'false' || completedOld === false || completedOld === 'abandoned'){
            record.completed = 'abandoned'
        }else{
            record.completed = 'ERR - '+completedOld
        }
        logger.info('OLD'+completedOld+' | NEW'+record.completed)

        let id = record._id
        delete record._id;
        delete record.Type;
        allUpdatePromises.push(dataCollection.updateOne({_id: id, type: 'CAMPAIGN'}, { $set: record }))
    }
    
    const allUpdatePromisesResult = await Promise.allSettled(allUpdatePromises);
}