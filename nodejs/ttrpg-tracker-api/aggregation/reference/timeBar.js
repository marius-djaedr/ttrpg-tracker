

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