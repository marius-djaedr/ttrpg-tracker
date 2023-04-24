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
        {type:'string',id:'Tooltip',label:'Tooltip',role:'tooltip'},
        {type:'string',id:'style',label:'style',role:'style'},
        {type:'date',id:'Start',label:'Start'},
        {type:'date',id:'End',label:'End'}
    ]
};

export function getBaseEntity() {
    return JSON.parse(JSON.stringify(BASE_ENTITY));
}

export function buildDatumRow(datum) {
    return [datum.row, datum.bar, getTooltipString(datum), datum.color, buildDateFunctionString(datum.startDate), buildDateFunctionString(datum.endDate)]
}

function buildDateFunctionString(date){
    return 'Date('+date.year+', '+(date.month-1)+', '+date.day+')';
}

function getTooltipString(datum){
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