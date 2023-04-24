const BASE_ENTITY = {
    chartTypeReadable: 'Time Bar',
    chartTypeGoogle: 'Timeline',
    options: {},
    settings: {
        packages: ['timeline']
    },
    columnDefinitions: [
        {type:'string',id:'Row Label',label:'Row Label'},
        {type:'string',id:'Bar Label',label:'Bar Label'},
        {type:'date',id:'Start',label:'Start'},
        {type:'date',id:'End',label:'End'}
    ]
};

export function getBaseEntity() {
    return JSON.parse(JSON.stringify(BASE_ENTITY));
}

export function buildDatumRow(datum) {
    return [datum.row, datum.bar, buildDateFunctionString(datum.startDate), buildDateFunctionString(datum.endDate)]
}

function buildDateFunctionString(date){
    return 'Date('+date.year+', '+(date.month-1)+', '+date.day+')';
}