
const aggMain = require('../src/aggregation/aggMain');
const logger = require('../src/logger');
const fs = require('fs');

const aggCollection = mockCollection([]);
const dataCollection = mockCollection(buildFromBackup());

aggMain.runAggregation(aggCollection, dataCollection)
    .then((afterSend) => {
        logger.info('BACK IN TEST');
        afterSend();
    }).catch(err => {logger.error(err)});

function mockCollection(findAllReturn){
    return {
        deleteMany: (c)=>{
            return {};
        },
        find: (d)=>{
            return {
                toArray: (e)=>{
                    return findAllReturn;
                }
            };
        },
        insertOne: async function(f){
            return {};
        }
    };
}

function buildFromBackup(){
    const fileDataString = fs.readFileSync('./test/backup-2023-04-29.json');
    const fileData = JSON.parse(fileDataString);

    const data = [];

    
    for(const id of Object.keys(fileData['CAMPAIGN'])){
        data.push(fileData['CAMPAIGN'][id]);
    }
    for(const id of Object.keys(fileData['CHARACTER'])){
        data.push(fileData['CHARACTER'][id]);
    }
    for(const id of Object.keys(fileData['SESSION'])){
        data.push(fileData['SESSION'][id]);
    }

    return data;
}