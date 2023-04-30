const logger = require('../logger');
const glob = require( 'glob' ).glob;
const path = require( 'path' );
const fs = require('fs/promises');
const moment = require('moment');

exports.aggregate = async function(aggInput, aggOutputs){
    logger.info('starting non-DB aggregation');

    const folderName = './output/agg-'+moment().format("YYYY-MM-DD-HH-mm-ss-SSS");
    
    await fs.mkdir(folderName);

    const writePromises = [];
    
    //find all aggregator files
    const aggregators = [];
    const aggFiles = await glob('./src/aggregation/nonDbAggs/**/*.js');
    aggFiles.forEach( function( file ) {
        aggregators.push(require(path.resolve(file)));
    });
    
    //start each aggregator
    const allAggPromises = [];
    for(const aggregator of aggregators){
        allAggPromises.push(aggregator.aggregate(folderName, aggInput, aggOutputs));
    }

    const allAggPromisesResult = await Promise.allSettled(allAggPromises);
    
    const filesToUpload = [];
    allAggPromisesResult.forEach((result) => {
        if(result.status === 'fulfilled'){
            const fileList = result.value;
            if(fileList !=null && fileList.length > 0){
                filesToUpload.push(...fileList);
            }
        }else{
            logger.error(result.reason);
        }
    });

    logger.info('Non-DB aggregation complete, folder: '+folderName);

    //TODO upload to google drive
    //TODO create non db agg for export to google sheet
}
