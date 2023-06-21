const AggUtils = require('../aggUtils');



exports.aggregate = async function(aggInput) {
    const playMap = {};
    const runMap = {};
    
    for(const sessionId of Object.keys(aggInput['SESSION'])){
        const session = aggInput['SESSION'][sessionId];
        processSession(playMap, runMap, session);
    }
    
    const dataMapping = buildDataMapping(playMap, runMap);
    
    const toReturn = [];
    for(const params of paramList){
        const result = buildReturn(dataMapping[params.name],params);
        toReturn.push(result);
    }
    return toReturn;
}
const monthDayArray = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

const paramList = [
    {
        name: 'Cumulative',
        options: {
            "chart":{
                "series":{
                    "0":{"targetAxisIndex":1},
                    "1":{"targetAxisIndex":1},
                    "2":{"targetAxisIndex":0}
                },
                "vAxes":{
                    "0":{"title":"Ratio","minValue":0,"maxValue":2},
                    "1":{"title":"Count"}
                }
            }
        },
        col4:'Ratio'
    },
    {
        name: 'Daily',
        options: {},
        col4:'Total'
    },
    {
        name: 'Monthly',
        options: {},
        col4:'Total'
    },
    {
        name: 'Quarterly',
        options: {},
        col4:'Total'
    },
    {
        name: 'Yearly',
        options: {},
        col4:'Total'
    }
];

function processSession(playMap, runMap, session){
    const toAdd = AggUtils.getSessionWeightedCount([session]);
    const date = session.date;
    if(session.playedWithoutCharacter == null || session.playedWithoutCharacter === '' || session.playedWithoutCharacter == true){
        manipulateMap(playMap,date,toAdd);
    }else{
        manipulateMap(runMap,date,toAdd);
    }
}

function manipulateMap(map, date, toAdd){
    const year = ''+date.getFullYear();
    const month = ''+date.getMonth();
    const day = ''+date.getDate();

    if(!Object.keys(map).includes(year)){
        map[year] = {};
    }
    if(!Object.keys(map[year]).includes(month)){
        map[year][month] = {};
    }
    if(!Object.keys(map[year][month]).includes(day)){
        map[year][month][day] = 0.0;
    }
    map[year][month][day] += toAdd;
}

function buildReturn(data, params){
    return {
        name: params.name,
        chartTypeReadable: 'Timeline',
        chartTypeGoogle: 'AnnotationChart',
        options: params.options,
        settings: {
            packages: ['annotationchart']
        },
        columnDefinitions: [
            {type:'date',id:'Date',label:'Date'},
            {type:'number',id:'Ran',label:'Ran'},
            {type:'number',id:'Played',label:'Played'},
            {type:'number',id:params.col4,label:params.col4}
        ],
        rows: data
    };
}

function buildDataMapping(playMap, runMap){

    const playYears = [...Object.keys(playMap)];
    const runYears = [...Object.keys(runMap)];

    playYears.sort();
    runYears.sort();

    const firstSessionYear = Math.min(playYears[0], runYears[0]);
    const currentYear = new Date().getFullYear();
    const currentMonth = new Date().getMonth();
    const currentDate = new Date().getDate();
    const currentQuarter = Math.floor(currentMonth/3);
    const currentMonthInQuarter = currentMonth%3;
    
    let ranCum = 0.0;
    let playedCum = 0.0;
    
    const dataMapping = {'Cumulative':[], 'Daily':[], 'Monthly':[], 'Quarterly':[], 'Yearly':[]};
    for(let y = firstSessionYear; y<=currentYear; y++){
        let ranYear = 0.0;
        let playedYear = 0.0;

        const playMap_year = playMap[y] == null? {} : playMap[y];
        const runMap_year = runMap[y] == null? {} : runMap[y];

        const maxQ = y == currentYear ? currentQuarter : 3;
        for(let q=0; q<=maxQ; q++){
            let ranQuarter = 0.0;
            let playedQuarter = 0.0;

            const maxMQ = (y == currentYear && q == currentQuarter) ? currentMonthInQuarter : 2;
            for(let mq=0; mq<=maxMQ; mq++){
                let ranMonth = 0.0;
                let playedMonth = 0.0;
                
                let m = q*3+mq;
                const playMap_month = playMap_year[m] == null? {} : playMap_year[m];
                const runMap_month = runMap_year[m] == null? {} : runMap_year[m];
                
                const maxD = (y == currentYear && m == currentMonth) ? currentDate : monthDayArray[m];
                for(let d=1; d <= maxD; d++){
                    let playValue = playMap_month[d] == null? 0.0 : playMap_month[d];
                    let runValue = runMap_month[d] == null? 0.0 : runMap_month[d];

                    let dateString = AggUtils.buildDateFunctionString(new Date(y, m, d));

                    ranCum += runValue;
                    playedCum += playValue;
                    let ratio = ranCum<0.001 ? null : playedCum/ranCum;
                    dataMapping['Cumulative'].push([dateString, ranCum, playedCum, ratio]);

                    dataMapping['Daily'].push([dateString, runValue, playValue, runValue+playValue]);

                    ranMonth += runValue;
                    playedMonth += playValue;
                }
                let totalMonth = ranMonth+playedMonth;
                dataMapping['Monthly'].push([AggUtils.buildDateFunctionString(new Date(y, m, 1)), ranMonth, playedMonth, totalMonth]);
                dataMapping['Monthly'].push([AggUtils.buildDateFunctionString(new Date(y, m, monthDayArray[m])), ranMonth, playedMonth, totalMonth]);
                
                ranQuarter += ranMonth;
                playedQuarter += playedMonth;
            }
            let totalQuarter = ranQuarter+playedQuarter;
            dataMapping['Quarterly'].push([AggUtils.buildDateFunctionString(new Date(y, q*3, 1)), ranQuarter, playedQuarter, totalQuarter]);
            dataMapping['Quarterly'].push([AggUtils.buildDateFunctionString(new Date(y, q*3+2, monthDayArray[q*3+2])), ranQuarter, playedQuarter, totalQuarter]);
            
            ranYear += ranQuarter;
            playedYear += playedQuarter;
        }
        let totalYear = ranYear+playedYear;
        dataMapping['Yearly'].push([AggUtils.buildDateFunctionString(new Date(y, 0, 1)), ranYear, playedYear, totalYear]);
        dataMapping['Yearly'].push([AggUtils.buildDateFunctionString(new Date(y, 11, 31)), ranYear, playedYear, totalYear]);
    }

    return dataMapping;
}