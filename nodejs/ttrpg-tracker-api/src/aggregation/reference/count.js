const AggUtils = require('../aggUtils');

exports.processAll = function(aggInput, params){
    const category_count_Map = {};
    
    for(const id of Object.keys(aggInput[params.elementType])){
        let element = aggInput[params.elementType][id];
        processElement(element, aggInput, params, category_count_Map);
    }
    return buildOutput(params, category_count_Map);
}

function processElement(element, aggInput, params, category_count_Map){
    const nameToSplit = params.categoryGetter(element);
    const names = nameToSplit==null? [] : nameToSplit.split('/');

    const weight = 1.0/names.length;

    const sessions = aggInput.sessionsForParent[element._id];
    const sessionCount = AggUtils.getSessionWeightedCount(sessions);
    const sessionWeight = sessionCount*weight;

    for(const name of names){
        if(!Object.keys(category_count_Map).includes(name)){
            category_count_Map[name] = {weightCount:0.,sessionCount:0.};
        }
        const obj = category_count_Map[name];
        obj.weightCount += weight;
        obj.sessionCount += sessionWeight;
    }
}

function buildOutput(params, category_count_Map){
    const weightData = [];
    const sessionsData = [];
    const ratioData = [];
    const names = [...Object.keys(category_count_Map)];
    names.sort();
    for(const name of names){
        const session = category_count_Map[name].sessionCount;
        const weight = category_count_Map[name].weightCount;
        const ratio = session/weight;

        weightData.push([name,weight]);
        sessionsData.push([name,session]);
        ratioData.push([name,ratio]);
    }

    const toReturn = [];

    if(params.include.sessionPie){
        toReturn.push({
            name: 'Sessions for '+params.name,
            chartTypeReadable: 'Pie',
            chartTypeGoogle: 'PieChart',
            options: {"chartArea":{"top":50,"height":400,"bottom":0},"legend":{"alignment":"center"}},
            settings: {
                packages: ['corechart']
            },
            columnDefinitions: [
                {type:'string',id:params.name,label:params.name},
                {type:'number',id:'sessions',label:'sessions'},
            ],
            rows: sessionsData
        });
    }

    if(params.include.sessionBar){
        toReturn.push({
            name: 'Sessions for '+params.name,
            chartTypeReadable: 'Bar',
            chartTypeGoogle: 'BarChart',
            options: {"width":1200,"vAxis":{"textStyle":{"fontSize":10}},"chartArea":{"top":50}},
            settings: {
                packages: ['corechart']
            },
            columnDefinitions: [
                {type:'string',id:params.name,label:params.name},
                {type:'number',id:'sessions',label:'sessions'},
            ],
            rows: sessionsData
        });
    }
    
    if(params.include.element){
        toReturn.push({
            name: params.elementType+'s for '+params.name,
            chartTypeReadable: 'Pie',
            chartTypeGoogle: 'PieChart',
            options: {"chartArea":{"top":50,"height":400,"bottom":0},"legend":{"alignment":"center"}},
            settings: {
                packages: ['corechart']
            },
            columnDefinitions: [
                {type:'string',id:params.name,label:params.name},
                {type:'number',id:params.elementType,label:params.elementType},
            ],
            rows: weightData
        });
    }

    

    if(params.include.ratio){
        toReturn.push({
            name: 'Sessions-per-'+params.elementType+' for '+params.name,
            chartTypeReadable: 'Bar',
            chartTypeGoogle: 'BarChart',
            options: {"width":1200,"vAxis":{"textStyle":{"fontSize":10}},"chartArea":{"top":50}},
            settings: {
                packages: ['corechart']
            },
            columnDefinitions: [
                {type:'string',id:params.name,label:params.name},
                {type:'number',id:'sessions/'+params.elementType,label:'sessions/'+params.elementType},
            ],
            rows: ratioData
        });
    }

    return toReturn;
}
