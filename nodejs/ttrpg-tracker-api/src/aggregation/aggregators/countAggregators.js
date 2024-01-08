const AggUtils = require('../aggUtils');
const count = require('../reference/count');

exports.aggregate = async function(aggInput) {
    const toReturn = [];
    for(const params of paramList){
        const result = count.processAll(aggInput,params);
        toReturn.push(...result);
    }
    return toReturn;
}

const paramList = [
    {
        name:'Campaign',
        elementType: 'CAMPAIGN',
        include:{sessionBar:true},
        categoryGetter: (campaign) => campaign.gm+': '+campaign.name+' ('+campaign.system+')'
    },
    {
        name:'GM',
        elementType: 'CAMPAIGN',
        include:{sessionPie:true,element:true,ratio:true},
        categoryGetter: (campaign) => campaign.gm
    },
    {
        name:'System',
        elementType: 'CAMPAIGN',
        include:{sessionPie:true,element:true,ratio:true},
        categoryGetter: (campaign) => campaign.system
    },
    {
        name:'Character',
        elementType: 'CHARACTER',
        include:{sessionBar:true},
        categoryGetter: (character) => character.name
    },
    {
        name:'Class',
        elementType: 'CHARACTER',
        include:{sessionPie:true,element:true,ratio:true},
        categoryGetter: (character) => character.classRole
    },
    {
        name:'Race',
        elementType: 'CHARACTER',
        include:{sessionPie:true,element:true,ratio:true},
        categoryGetter: (character) => character.race
    },
    {
        name:'Pronouns',
        elementType: 'CHARACTER',
        include:{sessionPie:true,element:true,ratio:true},
        categoryGetter: (character) => character.pronouns
    },
    {
        name:'Tragic Backstory',
        elementType: 'CHARACTER',
        include:{sessionPie:true,element:true,ratio:true},
        categoryGetter: (character) => character.tragicStory
    },
    {
        name:'Died in Game',
        elementType: 'CHARACTER',
        include:{sessionPie:true,element:true,ratio:true},
        categoryGetter: (character) => character.diedInGame
    }
];