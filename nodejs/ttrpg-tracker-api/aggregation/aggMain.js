const glob = require( 'glob' ).glob;
const path = require( 'path' );

module.exports = function(ctx) {
    this.collection = ctx.collection;

    this.runAggregation = async function(){
        const collection = this.collection;

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
        const aggInput = await buildAggInput(collection);
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
            }
        });

    // console.log(aggOutputs)
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
        return collection.insertOne({type: 'MAPPING', data: mappingData});
    }

    async function buildAggInput(collection){
        //TODO query the database and build ID mappings for each of the types
        return {};
    }

}