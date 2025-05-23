const glob = require( 'glob' ).glob;
const path = require( 'path' );
const logger = require('../logger');
const physicalAgg = require('./physicalAgg');


exports.runAggregation = async function(aggCollection, dataCollection){
    logger.info('delete prior aggregation')

    //first, delete all the aggregation stuff currently in the database
    const deleteResult = await aggCollection.deleteMany({})
    logger.info('delete complete, start aggregation')
    //currently don't do anything with the result
    
    //find all aggregator files
    const aggregators = [];
    const aggFiles = await glob('./src/aggregation/aggregators/**/*.js');
    aggFiles.forEach( function( file ) {
        aggregators.push(require(path.resolve(file)));
    });

    //start each aggregator
    const aggInput = await buildAggInput(dataCollection);
    const allAggPromises = [];
    for(const aggregator of aggregators){
        allAggPromises.push(aggregator.aggregate(aggInput));
    }

    //collect the output from each aggregator. NOTE: a single aggregator can produce multiple aggregations
    const aggOutputs = [];
    const allAggPromisesResult = await Promise.allSettled(allAggPromises);
    
    allAggPromisesResult.forEach((result) => {
        if(result.status === 'fulfilled'){
            const aggList = result.value;
            if(aggList !=null && aggList.length > 0){
                aggOutputs.push(...aggList);
            }
        }else{
            logger.error(result.reason);
        }
    });
    
    //start each insertion
    logger.info( 'Aggregation complete, starting save to DB')
    const allInsertPromises = [];
    for(const aggOutput of aggOutputs){
        allInsertPromises.push(
            aggCollection.insertOne({type: 'AGGREGATION', data: aggOutput})
                .then(doc => {
                    return {id:doc.insertedId, name:aggOutput.name, chartTypeReadable:aggOutput.chartTypeReadable}
                })
            );
    }

    //collect the output from each insertion
    const allInsertPromisesResult = await Promise.allSettled(allInsertPromises);
    const mappingData = [];
    allInsertPromisesResult.forEach((result) => {
        if(result.status === 'fulfilled'){
            mappingData.push(result.value);
        }
    });
    
    const insertMapResult = await aggCollection.insertOne({type: 'MAPPING', data: mappingData})
    logger.info( 'DB insert complete')

    return () =>{
        physicalAgg.aggregate(aggInput, aggOutputs).catch(err=>{logger.error(err)})
    };
}

async function buildAggInput(dataCollection){
    const allRecords = await dataCollection.find().toArray()

    const toReturn = {};
    for(const record of allRecords){
        let id = record._id;
        let type = record.type;
        if(!Object.keys(toReturn).includes(type)){
            toReturn[type] = {};
        }
        toReturn[type][id] = record;
    }

    const charactersForParent = {};
    for(const characterId of Object.keys(toReturn['CHARACTER'])){
        let character = toReturn['CHARACTER'][characterId];
        let parentId = character.parentId;
        if(!Object.keys(charactersForParent).includes(parentId)){
            charactersForParent[parentId] = [];
        }
        charactersForParent[parentId].push(character);
    }
    toReturn.charactersForParent = charactersForParent;

    const sessionsForParent = {};
    for(const sessionId of Object.keys(toReturn['SESSION'])){
        let session = toReturn['SESSION'][sessionId];
        //not really a better place to do this conversion
        session.date = new Date(session.date+"T00:00:00.000");

        let sessionParentId = session.parentId;
        if(!Object.keys(sessionsForParent).includes(sessionParentId)){
            sessionsForParent[sessionParentId] = [];
        }
        sessionsForParent[sessionParentId].push(session);

        if(Object.keys(toReturn['CHARACTER']).includes(sessionParentId)){
            //if parent is a character, also add this session to the campaign
            let characterParentId = toReturn['CHARACTER'][sessionParentId].parentId;
            if(!Object.keys(sessionsForParent).includes(characterParentId)){
                sessionsForParent[characterParentId] = [];
            }
            sessionsForParent[characterParentId].push(session);
        }
    }
    toReturn.sessionsForParent = sessionsForParent;

    return toReturn;
    }