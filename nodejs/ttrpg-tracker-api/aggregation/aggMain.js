const glob = require( 'glob' ).glob;
const path = require( 'path' );

module.exports = function(ctx) {
    this.collection = ctx.collection;
    this.client = ctx.client;
    this.inputCollection = this.client.db('TtrpgTracker').collection('TtrpgTracker');

    this.runAggregation = async function(){
// console.log('start')
        const collection = this.collection;
        const inputCollection = this.inputCollection;

        //first, delete all the aggregation stuff currently in the database
        const deleteResult = await collection.deleteMany({});
        //currently don't do anything with the result
        
        //find all aggregator files
        const aggregators = [];
        const aggFiles = await glob('./aggregation/aggregators/**/*.js');
        aggFiles.forEach( function( file ) {
            const aggClass = require(path.resolve(file));
            aggregators.push(new aggClass());
        });

// console.log(aggregators)
        //start each aggregator
        const aggInput = await buildAggInput(inputCollection);
        const allAggPromises = [];
        for(const aggregator of aggregators){
            allAggPromises.push(aggregator.aggregate(aggInput));
        }

        //collect the output from each aggregator. NOTE: a single aggregator can produce multiple aggregations
        const aggOutputs = [];
        const allAggPromisesResult = await Promise.allSettled(allAggPromises);
// console.log(allAggPromisesResult)
        allAggPromisesResult.forEach((result) => {
            if(result.status === 'fulfilled'){
                const aggList = result.value;
                if(aggList !=null && aggList.length > 0){
                    aggOutputs.push(...aggList);
                }
            }else{
                console.error(result.reason);
            }
        });

//  console.log(aggOutputs)
        //start each insertion
        const allInsertPromises = [];
        for(const aggOutput of aggOutputs){
            allInsertPromises.push(
                collection.insertOne({type: 'AGGREGATION', data: aggOutput})
                    .then(doc => {
                        return {id:doc.insertedId, name:aggOutput.name, chartTypeReadable:aggOutput.chartTypeReadable}
                    })
                );
        }

        //collect the output from each insertion
        const allInsertPromisesResult = await Promise.allSettled(allInsertPromises);
// console.log(allInsertPromisesResult)
        const mappingData = [];
        allInsertPromisesResult.forEach((result) => {
            if(result.status === 'fulfilled'){
                mappingData.push(result.value);
            }
        });
        
// console.log(mappingData)
        const insertMapResult = await collection.insertOne({type: 'MAPPING', data: mappingData});

        //TODO also do physical output
    }

    async function buildAggInput(inputCollection){
        const allRecords = await inputCollection.find().toArray();

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
            session.date = new Date(session.date);

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

}