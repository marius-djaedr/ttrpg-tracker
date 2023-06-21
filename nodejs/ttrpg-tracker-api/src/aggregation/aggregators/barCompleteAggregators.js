const AggUtils = require('../aggUtils');

exports.aggregate = async function(aggInput) {
    const dataMapping = {granular:{}, gm:{}, system: {}};
    
    for(const id of Object.keys(aggInput['CAMPAIGN'])){
        const campaign = aggInput['CAMPAIGN'][id];
        processCampaign(dataMapping, campaign, aggInput);
    }

    return [buildGranularReturn(dataMapping.granular), buildOtherReturn(dataMapping.gm, 'GM'), buildOtherReturn(dataMapping.system, 'System')]
}

function processCampaign(dataMapping, campaign, aggInput){
    const sessions = aggInput.sessionsForParent[campaign._id];
    const sessionCount = AggUtils.getSessionWeightedCount(sessions);

    if(!Object.keys(dataMapping.granular).includes(''+sessionCount)){
        dataMapping.granular[''+sessionCount] = {abandoned:0,completed:0,ongoing:0};
    }
    const granularObj = dataMapping.granular[''+sessionCount];

    if(!Object.keys(dataMapping.gm).includes(campaign.gm)){
        dataMapping.gm[campaign.gm] = {abandoned:0,completed_oneshot:0,completed_short:0,completed_mid:0,completed_long:0,completed_extralong:0,ongoing:0};
    }
    const gmObj = dataMapping.gm[campaign.gm];

    if(!Object.keys(dataMapping.system).includes(campaign.system)){
        dataMapping.system[campaign.system] = {abandoned:0,completed_oneshot:0,completed_short:0,completed_mid:0,completed_long:0,completed_extralong:0,ongoing:0};
    }
    const systemObj = dataMapping.system[campaign.system];

    if(campaign.completed==null || campaign.completed===''){
        granularObj.ongoing++
        gmObj.ongoing++
        systemObj.ongoing++
    }else if (campaign.completed===false || campaign.completed==='false'){
        granularObj.abandoned++
        gmObj.abandoned++
        systemObj.abandoned++
    }else{
        granularObj.completed++
        if(sessionCount < 3){
            gmObj.completed_oneshot++
            systemObj.completed_oneshot++
        }else if(sessionCount < 7){
            gmObj.completed_short++
            systemObj.completed_short++
        }else if(sessionCount < 14){
            gmObj.completed_mid++
            systemObj.completed_mid++
        }else if(sessionCount < 21){
            gmObj.completed_long++
            systemObj.completed_long++
        }else{
            gmObj.completed_extralong++
            systemObj.completed_extralong++
        }
    }
}

function buildGranularReturn(dataMapping){
    const keys = [...Object.keys(dataMapping)]
    keys.sort((a,b)=>a-b)

    const data = [];
    for(const key of keys){
        const obj = dataMapping[key]
        data.push([key,obj.abandoned, obj.completed, obj.ongoing])
    }

    return commonBuildReturn(data, {
        name: 'Granulated Completion',
        columnDefinitions: [
            {type:'number',id:'Number Sessions',label:'Number Sessions'},
            {type:'number',id:'Abandoned',label:'Abandoned'},
            {type:'number',id:'Completed',label:'Completed'},
            {type:'number',id:'Ongoing',label:'Ongoing'}
        ]
    })
}

function buildOtherReturn(dataMapping, type){
    const keys = [...Object.keys(dataMapping)]
    keys.sort()

    const data = [];
    for(const key of keys){
        const obj = dataMapping[key]
        data.push([key, obj.abandoned, obj.completed_oneshot, obj.completed_short, obj.completed_mid, obj.completed_long, obj.completed_extralong, obj.ongoing])
    }

    return commonBuildReturn(data, {
        name: type+' Completion',
        columnDefinitions: [
            {type:'string',id:'Name',label:'Name'},
            {type:'number',id:'Abandoned',label:'Abandoned'},
            {type:'number',id:'Completed One-Shot Campaigns [0,3)',label:'Completed One-Shot Campaigns [0,3)'},
            {type:'number',id:'Completed Short Campaigns [3,7)',label:'Completed Short Campaigns [3,7)'},
            {type:'number',id:'Completed Mid Campaigns [7,14)',label:'Completed Mid Campaigns [7,14)'},
            {type:'number',id:'Completed Long Campaigns [14,21)',label:'Completed Long Campaigns [14,21)'},
            {type:'number',id:'Completed Extra Long Campaigns [21,+)',label:'Completed Extra Long Campaigns [21,+)'},
            {type:'number',id:'Ongoing',label:'Ongoing'}
        ]
    })
}


function commonBuildReturn(data, params){
    return {
        name: params.name,
        chartTypeReadable: 'Bar',
        chartTypeGoogle: 'BarChart',
        options: {"width":1000,"isStacked":true,"vAxis":{"textStyle":{"fontSize":10}},"chartArea":{"top":50}},
        settings: {
            packages: ['corechart']
        },
        columnDefinitions: params.columnDefinitions,
        rows: data
    };
}