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
    const docs = await dataCollection.find({type:'CHARACTER'}).toArray()
    
    const allUpdatePromises = [];
    for(const record of docs){
        let old = record.diedInGame
        if(old === 'true' || old === true || old === 'Died'){
            record.diedInGame = 'Died'
        }else if(old === 'false' || old === false || old === 'Lived'){
            record.diedInGame = 'Lived'
        }else if(old === '' || old === null || old === 'Alive For Now'){
            record.diedInGame = 'Alive For Now'
        }else{
            record.diedInGame = 'ERR - '+old
        }
        logger.info('OLD'+old+' | NEW'+record.diedInGame)

        let id = record._id
        delete record._id;
        delete record.Type;
        allUpdatePromises.push(dataCollection.updateOne({_id: id, type: 'CHARACTER'}, { $set: record }))
    }
    
    const allUpdatePromisesResult = await Promise.allSettled(allUpdatePromises);
}