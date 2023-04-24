const glob = require( 'glob' ).glob;
const path = require( 'path' );

module.exports = function(ctx) {
    this.collection = ctx.collection;

    this.runAggregation = async function(){
        const collection = this.collection;
        const deleteResult = await collection.deleteMany({});
        //currently don't do anything with the result

        const aggregators = [];
        const aggFiles = await glob('./aggregation/aggregators/**/*.js');
        aggFiles.forEach( function( file ) {
            const aggClass = require(path.resolve(file));
            aggregators.push(new aggClass());
        });

        const aggInput = await buildAggInput(collection);
        const allPromises = [];
        
        for(const aggregator of aggregators){
            allPromises.push(aggregate(aggregator, aggInput, collection));
        }

        const allPromisesResult = await Promise.allSettled(allPromises);
        const mappingData = [];
        allPromisesResult.forEach((result) => {
            if(result.status === 'fulfilled'){
                mappingData.push(result.value);
            }
        });
        
        return collection.insertOne({type: 'MAPPING', data: mappingData});
    }

    async function buildAggInput(collection){
        //TODO query the database and build ID mappings for each of the types
        return {};
    }

    async function aggregate(aggregator, aggInput, collection){
        let agg = await aggregator.aggregate(aggInput);
        let doc = await collection.insertOne({type: 'AGGREGATION', data: agg});
        return {id:doc.insertedId, name:agg.name, chartTypeReadable:agg.chartTypeReadable};
    }
}