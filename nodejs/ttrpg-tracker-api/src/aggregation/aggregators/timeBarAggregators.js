const AggUtils = require('../aggUtils');
const timeBar = require('../reference/timeBar');


exports.aggregate = async function(aggInput){
    const toReturn = [];
    for(const params of paramList){
        const result = timeBar.processAll(aggInput,params);
        toReturn.push(result);
    }
    return toReturn;
}

const fnCampName = (session, aggInput) => AggUtils.getCampaignFromSession(session, aggInput).name;
const fnCampGm = (session, aggInput) => AggUtils.getCampaignFromSession(session, aggInput).gm;
const fnCampSystem = (session, aggInput) => AggUtils.getCampaignFromSession(session, aggInput).system;
const fnCampCompleted = (session, aggInput) => AggUtils.convertBooleanToWord(AggUtils.getCampaignFromSession(session, aggInput).completed);

const fnCharName = (session, aggInput) => AggUtils.getCharacterFromSession(session, aggInput).name;
const fnCharRace = (session, aggInput) => AggUtils.getCharacterFromSession(session, aggInput).race;
const fnCharClass = (session, aggInput) => AggUtils.getCharacterFromSession(session, aggInput).classRole;
const fnCharGender = (session, aggInput) => AggUtils.getCharacterFromSession(session, aggInput).gender;
const fnCharDied = (session, aggInput) => AggUtils.convertBooleanToWord(AggUtils.getCharacterFromSession(session, aggInput).diedInGame);
const fnCharTragic = (session, aggInput) => AggUtils.convertBooleanToWord(AggUtils.getCharacterFromSession(session, aggInput).tragicStory);

const paramList = [
    {
        chartName: 'GM - Campaign - Completed',
        categoryName: 'Completed',
        session_rowNameFunc: fnCampGm,
        session_barNameFunc: fnCampName,
        session_categoryFunc: fnCampCompleted
    },
    {
        chartName: 'System - Campaign - Completed',
        categoryName: 'Completed',
        session_rowNameFunc: fnCampSystem,
        session_barNameFunc: fnCampName,
        session_categoryFunc: fnCampCompleted
    },
    {
        chartName: 'GM - System',
        session_rowNameFunc: fnCampGm,
        session_barNameFunc: fnCampSystem,
    },
    {
        chartName: 'System - GM',
        session_rowNameFunc: fnCampSystem,
        session_barNameFunc: fnCampGm,
    },
    {
        chartName: 'GM - Character',
        session_rowNameFunc: fnCampGm,
        session_barNameFunc: fnCharName,
    },
    {
        chartName: 'System - Character',
        session_rowNameFunc: fnCampSystem,
        session_barNameFunc: fnCharName,
    },
    {
        chartName: 'Campaign - Character',
        session_rowNameFunc: fnCampName,
        session_barNameFunc: fnCharName,
    },
    {
        chartName: 'Race - Character - Gender',
        categoryName: 'Gender',
        session_rowNameFunc: fnCharRace,
        session_barNameFunc: fnCharName,
        session_categoryFunc: fnCharGender
    },
    {
        chartName: 'Race - Character - Died',
        categoryName: 'Died',
        session_rowNameFunc: fnCharRace,
        session_barNameFunc: fnCharName,
        session_categoryFunc: fnCharDied
    },
    {
        chartName: 'Race - Character - Tragic',
        categoryName: 'Tragic',
        session_rowNameFunc: fnCharRace,
        session_barNameFunc: fnCharName,
        session_categoryFunc: fnCharTragic
    },
    {
        chartName: 'Class - Character - Gender',
        categoryName: 'Gender',
        session_rowNameFunc: fnCharClass,
        session_barNameFunc: fnCharName,
        session_categoryFunc: fnCharGender
    },
    {
        chartName: 'Class - Character - Died',
        categoryName: 'Died',
        session_rowNameFunc: fnCharClass,
        session_barNameFunc: fnCharName,
        session_categoryFunc: fnCharDied
    },
    {
        chartName: 'Class - Character - Tragic',
        categoryName: 'Tragic',
        session_rowNameFunc: fnCharClass,
        session_barNameFunc: fnCharName,
        session_categoryFunc: fnCharTragic
    },
    {
        chartName: 'Gender - Character - Died',
        categoryName: 'Died',
        session_rowNameFunc: fnCharGender,
        session_barNameFunc: fnCharName,
        session_categoryFunc: fnCharDied
    },
    {
        chartName: 'Gender - Character - Tragic',
        categoryName: 'Tragic',
        session_rowNameFunc: fnCharGender,
        session_barNameFunc: fnCharName,
        session_categoryFunc: fnCharTragic
    },
    {
        chartName: 'Died - Character - Gender',
        categoryName: 'Gender',
        session_rowNameFunc: fnCharDied,
        session_barNameFunc: fnCharName,
        session_categoryFunc: fnCharGender
    },
    {
        chartName: 'Died - Character - Tragic',
        categoryName: 'Tragic',
        session_rowNameFunc: fnCharDied,
        session_barNameFunc: fnCharName,
        session_categoryFunc: fnCharTragic
    },
    {
        chartName: 'Tragic - Character - Gender',
        categoryName: 'Gender',
        session_rowNameFunc: fnCharTragic,
        session_barNameFunc: fnCharName,
        session_categoryFunc: fnCharGender
    },
    {
        chartName: 'Tragic - Character - Died',
        categoryName: 'Died',
        session_rowNameFunc: fnCharTragic,
        session_barNameFunc: fnCharName,
        session_categoryFunc: fnCharDied
    }
];