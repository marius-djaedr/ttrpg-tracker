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
            logger.info("added played, removing playedWithoutCharacter")
            dataCollection.updateMany({}, {$unset:{"playedWithoutCharacter":1}}).then(ret =>{
                logger.info("job's done")
                process.exit(1)
            })
        })
    })
    .catch(err =>{
        logger.info('An error occurred while attempting to connect to MongoDB', err)
        process.exit(1)
    })

async function queryAndUpdate(dataCollection){
    const docs = await dataCollection.find({type:'SESSION'}).toArray()
    
    const allUpdatePromises = [];
    for(const record of docs){
        let playedWithoutCharacter = record.playedWithoutCharacter
        if(playedWithoutCharacter === true || playedWithoutCharacter === "true"){
            record.played = 'Played (no character)'
        }
        else if(playedWithoutCharacter === false || playedWithoutCharacter === "false"){
            record.played = 'Ran'
        }
        else if(playedWithoutCharacter === null || playedWithoutCharacter === ""){
            record.played = 'Played'
        }
        else{
            record.played = 'TO UPDATE'
        }

        let id = record._id
        delete record._id;
        delete record.Type;
        allUpdatePromises.push(dataCollection.updateOne({_id: id, type: 'SESSION'}, { $set: record }))
    }
    
    const allUpdatePromisesResult = await Promise.allSettled(allUpdatePromises);
}