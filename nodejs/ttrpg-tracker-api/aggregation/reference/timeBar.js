

module.exports = function(params) {
    this.chartName = params.chartName;
    this.categoryName = params.categoryName;

    this.session_rowNameFunc = params.session_rowNameFunc;
    this.session_barNameFunc = params.session_barNameFunc;
    this.session_categoryFunc = params.session_categoryFunc;

    
    this.processAll = function(aggInput){
        const categoryCountMap = {};
        const row_bar_session_Map = {};

        for(const session of aggInput.sessions){
            processSession(session, aggInput, categoryCountMap, row_bar_session_Map);
        }

        const data = buildData(row_bar_session_Map);
        //TODO
    }

    function processSession(session, aggInput, categoryCountMap, row_bar_session_Map){
        let rowNameToSplit = this.session_rowNameFunc(session, aggInput);
        let barNameToSplit = this.session_barNameFunc(session, aggInput);

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
                    if(this.session_categoryFunc != null){
                        category = this.session_categoryFunc(session, aggInput);
            
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

    function buildData(row_bar_session_Map){
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
                    data.push(buildDatumRow(rowName,barName,sessionRange.startDate,sessionRange.endDate,barMap.category));
                }
            }
        }
        return data;
    }

    function calculateSessionRanges(sessionDates){
        //TODO
        return [];
    }


    function buildDatumRow(row,bar,startDate,endDate,category) {
        if(this.categoryName != null){
            return [row, 
                bar, 
                getTooltipString(row,bar,startDate,endDate,category), 
                getColorString(category), 
                buildDateFunctionString(startDate), 
                buildDateFunctionString(endDate)];
        } else {
            return [row, 
                bar, 
                buildDateFunctionString(startDate), 
                buildDateFunctionString(endDate)];
        }
    }

    
    function getColorString(category){
      //TODO
      return '';
    }
    
    function buildDateFunctionString(date){
        return 'Date('+date.getFullYear()+', '+date.getMonth()+', '+date.getDate()+')';
    }
    
    function getTooltipString(row,bar,startDate,endDate,category){
        let retString =     '<ul class="google-visualization-tooltip-item-list">';
        retString +=            '<li class="google-visualization-tooltip-item">';
        retString +=                '<span style="font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:bold;">';
        retString += datum.bar;
        retString +=                '</span>';
        retString +=            '</li>';
        retString +=        '</ul>';
        retString +=        '<div class="google-visualization-tooltip-separator"></div>';
        retString +=        '<ul class="google-visualization-tooltip-action-list">';
        retString +=            '<li data-logicalname="action#" class="google-visualization-tooltip-action">';
        retString +=                '<span style="font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:bold;">';
        retString += datum.row;
        retString +=                    ': ';
        retString +=                '</span>';
        retString +=                '<span style="font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:none;">';
        retString += datum.startDate.year+'-'+datum.startDate.month+'-'+datum.startDate.day;
        retString +=                    ' - ';
        retString += datum.endDate.year+'-'+datum.endDate.month+'-'+datum.endDate.day;
        retString +=                '</span>';
        retString +=            '</li>';
        retString +=            '<li data-logicalname="action#" class="google-visualization-tooltip-action">';
        retString +=                '<span style="font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:bold;">';
        retString += datum.categoryName;
        retString +=                    ': ';
        retString +=                '</span>';
        retString +=                '<span style="font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:none;">';
        retString += datum.category;
        retString +=                '</span>';
        retString +=            '</li>';
        retString +=        '</ul>';
        return retString;
    }

}

//////////////////////////////////////////////////////////////////////////
//OLD

// const BASE_ENTITY = {
//     chartTypeReadable: 'Time Bar',
//     chartTypeGoogle: 'Timeline',
//     options: {},
//     settings: {
//         packages: ['timeline']
//     },
//     columnDefinitions: [
//         {type:'string',id:'Row Label',label:'Row Label'},
//         {type:'string',id:'Bar Label',label:'Bar Label'},
//         {type:'date',id:'Start',label:'Start'},
//         {type:'date',id:'End',label:'End'}
//     ]
// };

// export function getBaseEntity() {
//     return JSON.parse(JSON.stringify(BASE_ENTITY));
// }

// export function buildDatumRow(datum) {
//     return [datum.row, datum.bar, buildDateFunctionString(datum.startDate), buildDateFunctionString(datum.endDate)]
// }

// function buildDateFunctionString(date){
//     return 'Date('+date.year+', '+(date.month-1)+', '+date.day+')';
// }