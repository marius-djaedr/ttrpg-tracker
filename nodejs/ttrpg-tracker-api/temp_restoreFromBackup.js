const logger = require('./src/logger');
const PropertiesReader = require('properties-reader');
const prop = PropertiesReader('./mongodb.properties');
const mongodb = require('mongodb').MongoClient;
const fs = require('fs');

const backupFile = './output/agg-2024-01-08-13-58-47-951/backup.json';
logger.info('reading backup '+backupFile);
const data = fs.readFileSync(backupFile,{ encoding: 'utf8' });
const backupObject = JSON.parse(data);

logger.info('mongo setup start')
// establish connection to mongodb atlas
mongodb.connect(prop.get('mongodb.url'))
    .then(client => {
        logger.info('mongo done')
        const dataCollection = client.db('TtrpgTracker').collection('TtrpgTracker');

        queryAndUpdate(backupObject, dataCollection).then(ret =>{
                logger.info("job's done")
                process.exit(1)
        })

    })
    .catch(err =>{
        logger.info('An error occurred while attempting to connect to MongoDB', err)
        process.exit(1)
    })


async function queryAndUpdate(backupObj, dataCollection){
    
    const docs = await dataCollection.find({type:'SESSION'}).toArray()
    
    const allUpdatePromises = [];
    for(const record of docs){
        let id = record._id
        
        let backupSession = backupObj['SESSION'][id]

        record.shortSession = backupSession.shortSession;
        record.played = backupSession.played;

        delete record._id;
        delete record.Type;
        allUpdatePromises.push(dataCollection.updateOne({_id: id, type: 'SESSION'}, { $set: record }))
    }
    
    const allUpdatePromisesResult = await Promise.allSettled(allUpdatePromises);
}