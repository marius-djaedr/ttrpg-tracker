const sheets = require('../../google/sheets')
const logger = require('../../logger');
const AggUtils = require('../aggUtils');
const moment = require('moment');

const SPREADSHEET_ID = "11lEezEg9rNhk_e7ZCfvW3rZ8O9-L_7gKrKAQy4q5Hqo";

exports.aggregate = async function(folderName, aggInput, aggOutputs){
    logger.info('Starting sheet ref export')
    const data = [];

    for(const id of Object.keys(aggInput['CAMPAIGN'])){
        const campaign = aggInput['CAMPAIGN'][id];
        processCampaign(data, campaign, aggInput);
    }
    
    data.push(['','','','','','','','','','']);
    data.push(['','','','','','','','','','']);
    data.push(['','','','','','','','','','']);
    data.push(['','','','','','','','','','']);
    data.push(['','','','','','','','','','']);

    await sheets.writeToSheet(SPREADSHEET_ID, 'A2', 'RAW', data)

    //return empty because there is no physical file to write
    return [];
}

function processCampaign(data, campaign, aggInput){
    const characters = aggInput.charactersForParent[campaign._id]
    if(characters != null){
        for(const character of characters){
            addRow(data, campaign, character.name, calculateSessions(aggInput.sessionsForParent[character._id]))
        }
    }else{
        addRow(data, campaign, 'N/A', calculateSessions(aggInput.sessionsForParent[campaign._id]))
    }
}

function addRow(data, campaign, characterName, sessions){
    const datum = [];
    
    datum.push(campaign.name)
    datum.push(campaign.gm)
    datum.push(campaign.system)
    datum.push(characterName)
    datum.push(sessions.first)
    datum.push(sessions.last)
    datum.push(sessions.total)
    datum.push('')
    datum.push('')
    datum.push('')
    datum.push('')
    datum.push('')

    data.push(datum);
}

function calculateSessions(sessions){
    const total = AggUtils.getSessionWeightedCount(sessions)

    const sessionDates = [];
    for(const session of sessions){
        sessionDates.push(moment(session.date).format("YYYY-MM-DD"))
    }
    sessionDates.sort()

    return {first:sessionDates[0], last:sessionDates[sessionDates.length - 1], total:total}
}