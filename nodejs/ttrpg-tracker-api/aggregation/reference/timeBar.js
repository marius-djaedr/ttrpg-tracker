const COLOR_ORDER = ['#6E3E98', '#A2CA7D', '#98413E', '#7DC8CA', '#A67DCA', '#69983E', '#CA807D', '#3E9598'];

exports.processAll = function(aggInput,params){
    const categoryCountMap = {};
    const row_bar_session_Map = {};

    for(const sessionId of Object.keys(aggInput['SESSION'])){
        let session = aggInput['SESSION'][sessionId];
        processSession(session, aggInput,params, categoryCountMap, row_bar_session_Map);
    }

    const categoryColorMap = buildColorMap(categoryCountMap);

    const data = buildData(params, row_bar_session_Map,categoryColorMap);

    const columnDefinitions = params.categoryName==null ? [
        {type:'string',id:'Row Label',label:'Row Label'},
        {type:'string',id:'Bar Label',label:'Bar Label'},
        {type:'date',id:'Start',label:'Start'},
        {type:'date',id:'End',label:'End'}
    ] : [
        {type:'string',id:'Row Label',label:'Row Label'},
        {type:'string',id:'Bar Label',label:'Bar Label'},
        {type:'string',id:'Tooltip',label:'Tooltip',role:'tooltip'},
        {type:'string',id:'style',label:'style',role:'style'},
        {type:'date',id:'Start',label:'Start'},
        {type:'date',id:'End',label:'End'}
    ];

    return {
        name: params.chartName,
        chartTypeReadable: 'Time Bar',
        chartTypeGoogle: 'Timeline',
        options: {},
        settings: {
            packages: ['timeline']
        },
        columnDefinitions: columnDefinitions,
        rows: data
    };
}

function processSession(session, aggInput, params, categoryCountMap, row_bar_session_Map){
    let rowNameToSplit = params.session_rowNameFunc(session, aggInput);
    let barNameToSplit = params.session_barNameFunc(session, aggInput);

    let rowNames = rowNameToSplit==null? [] : rowNameToSplit.split('/');
    let barNames = barNameToSplit==null? [] : barNameToSplit.split('/');
    
    for(const rowName of rowNames){
        if(!Object.keys(row_bar_session_Map).includes(rowName)){
            row_bar_session_Map[rowName] = {};
        }
        const rowMap = row_bar_session_Map[rowName];
        for(const barName of barNames){
            if(!Object.keys(rowMap).includes(barName)){
                let category = null;
                if(params.session_categoryFunc != null){
                    category = params.session_categoryFunc(session, aggInput);
        
                    if(!Object.keys(categoryCountMap).includes(category)){
                        categoryCountMap[category] = 0;
                    }
                    categoryCountMap[category]++;
                }
                rowMap[barName] = {category:category, sessionDates:[]};
            }
            rowMap[barName].sessionDates.push(session.date);
        }
    }
}

function buildColorMap(categoryCountMap){
    const categoryList = [...Object.keys(categoryCountMap)];
    categoryList.sort(function(a,b){return categoryCountMap[b]-categoryCountMap[a]});

    const colorMap = {};
    for(let i=0;i<categoryList.length;i++){
        colorMap[categoryList[i]] = COLOR_ORDER[i];
    }
    return colorMap;
}

function buildData(params, row_bar_session_Map,categoryColorMap){
    const data = [];
    const rowNames = [...Object.keys(row_bar_session_Map)];
    rowNames.sort();
    for(const rowName of rowNames){
        const rowMap = row_bar_session_Map[rowName];
        const barNames = [...Object.keys(rowMap)];
        barNames.sort();
        for(const barName of barNames){
            const barMap = rowMap[barName];

            const sessionRanges = calculateSessionRanges(barMap.sessionDates);
            for(const sessionRange of sessionRanges){
                data.push(buildDatumRow(params, rowName,barName,sessionRange.startDate,sessionRange.endDate,barMap.category,categoryColorMap[barMap.category]));
            }
        }
    }
    return data;
}

function calculateSessionRanges(sessionDates){
    if(sessionDates == null || sessionDates.length == 0){
        return [];
    }
    sessionDates.sort(function(a,b){return a.getTime()-b.getTime()});

    let prevDate = sessionDates[0];
    const rawRanges = [[prevDate]];
    for(let i=1;i<sessionDates.length;i++){
        const curDate = sessionDates[i];
        if(curDate.getTime() - prevDate.getTime() <= 3024000000){
            rawRanges[rawRanges.length-1].push(curDate);
        } else{
            rawRanges.push([curDate]);
        }
        prevDate = curDate;
    }

    const toReturn = [];
    for(const rawRange of rawRanges){
        const obj = {startDate:rawRange[0]};
        if(rawRange.length==1){
            obj.endDate = rawRange[0];
        }else{
            obj.endDate = rawRange[rawRange.length-1];
        }
        toReturn.push(obj);
    }

    return toReturn;
}


function buildDatumRow(params, row,bar,startDate,endDate,category,color) {
    if(params.categoryName != null){
        return [row, 
            bar, 
            getTooltipString(params,row,bar,startDate,endDate,category), 
            color, 
            buildDateFunctionString(startDate), 
            buildDateFunctionString(endDate)];
    } else {
        return [row, 
            bar, 
            buildDateFunctionString(startDate), 
            buildDateFunctionString(endDate)];
    }
}


function buildDateFunctionString(date){
    return 'Date('+date.getFullYear()+', '+date.getMonth()+', '+date.getDate()+')';
}

function getTooltipString(params,row,bar,startDate,endDate,category){
    let retString =     '<ul class="google-visualization-tooltip-item-list">';
    retString +=            '<li class="google-visualization-tooltip-item">';
    retString +=                '<span style="font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:bold;">';
    retString += bar;
    retString +=                '</span>';
    retString +=            '</li>';
    retString +=        '</ul>';
    retString +=        '<div class="google-visualization-tooltip-separator"></div>';
    retString +=        '<ul class="google-visualization-tooltip-action-list">';
    retString +=            '<li data-logicalname="action#" class="google-visualization-tooltip-action">';
    retString +=                '<span style="font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:bold;">';
    retString += row;
    retString +=                    ': ';
    retString +=                '</span>';
    retString +=                '<span style="font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:none;">';
    retString += startDate.getFullYear()+'-'+(startDate.getMonth()+1)+'-'+startDate.getDate();
    retString +=                    ' - ';
    retString += endDate.getFullYear()+'-'+(endDate.getMonth()+1)+'-'+endDate.getDate();
    retString +=                '</span>';
    retString +=            '</li>';
    retString +=            '<li data-logicalname="action#" class="google-visualization-tooltip-action">';
    retString +=                '<span style="font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:bold;">';
    retString += params.categoryName;
    retString +=                    ': ';
    retString +=                '</span>';
    retString +=                '<span style="font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:none;">';
    retString += category;
    retString +=                '</span>';
    retString +=            '</li>';
    retString +=        '</ul>';
    return retString;
}