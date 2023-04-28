const AggUtils = require('../aggUtils');
const monthDayArray = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

module.exports = function() {

    this.aggregate = async function(aggInput) {
        const playMap = {};
        const runMap = {};

        for(const sessionId of Object.keys(aggInput['SESSION'])){
            const session = aggInput['SESSION'][sessionId];
            processSession(playMap, runMap, session);
        }

        const data = buildData(playMap, runMap);
        return buildReturn(data);
    }

    function processSession(playMap, runMap, session){
        const toAdd = (session.shortSession != null && session.shortSession == true)? 0.5 : 1.0;
        const date = session.date;
        console.log(session.pla)
        if(session.playedWithoutCharacter == null || session.playedWithoutCharacter == true){
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

    function buildData(playMap, runMap){
        const playYears = [...Object.keys(playMap)];
        const runYears = [...Object.keys(runMap)];

        playYears.sort();
        runYears.sort();

        const firstSessionYear = Math.min(playYears[0], runYears[0]);
        const currentYear = new Date().getFullYear();
        const currentMonth = new Date().getMonth();
        
        let ran = 0.0;
        let played = 0.0;

        const data = [];
        for(let y = firstSessionYear; y<currentYear; y++){
            const playMap_year = playMap[y] == null? {} : playMap[y];
            const runMap_year = runMap[y] == null? {} : runMap[y];
            for(let m = 0; m<12; m++){
                const playMap_month = playMap_year[m] == null? {} : playMap_year[m];
                const runMap_month = runMap_year[m] == null? {} : runMap_year[m];
                for(let d=1; d <= monthDayArray[m]; d++){
                    played += playMap_month[d] == null? 0.0 : playMap_month[d];
                    ran += runMap_month[d] == null? 0.0 : runMap_month[d];
                    let ratio = ran<0.001 ? null : played/ran;
                    data.push([AggUtils.buildDateFunctionString(new Date(y, m, d)), ran, played, ratio]);
                }
            }
        }
        
        const playMap_year = playMap[currentYear] == null? {} : playMap[currentYear];
        const runMap_year = runMap[currentYear] == null? {} : runMap[currentYear];
        for(let m = 0; m<=currentMonth; m++){
            const playMap_month = playMap_year[m] == null? {} : playMap_year[m];
            const runMap_month = runMap_year[m] == null? {} : runMap_year[m];
            for(let d=1; d <= monthDayArray[m]; d++){
                played += playMap_month[d] == null? 0.0 : playMap_month[d];
                ran += runMap_month[d] == null? 0.0 : runMap_month[d];
                let ratio = ran<0.001 ? null : played/ran;
                data.push([AggUtils.buildDateFunctionString(new Date(currentYear, m, d)), ran, played, ratio]);
            }
        }


        return data;
    }


    function buildReturn(data){
        return [{
            name: 'Cumulative',
            chartTypeReadable: 'Timeline',
            chartTypeGoogle: 'AnnotationChart',
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
            settings: {
                packages: ['annotationchart']
            },
            columnDefinitions: [
                {type:'date',id:'Date',label:'Date'},
                {type:'number',id:'Ran',label:'Ran'},
                {type:'number',id:'Played',label:'Played'},
                {type:'number',id:'Ratio',label:'Ratio'}
            ],
            rows: data
        }];
    }
}