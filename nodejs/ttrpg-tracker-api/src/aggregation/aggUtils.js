
exports.convertBooleanToWord = function(bool) {
    if(bool == null){
        return 'Unknown';
    }else if(bool || bool == 'true'){
        return 'Yes';
    }else{
        return 'No';
    }
}

exports.getCampaignFromSession = function(session, aggInput){
    const PWC = session.playedWithoutCharacter;
    if(PWC == null){
        const character = aggInput['CHARACTER'][session.parentId];
        return aggInput['CAMPAIGN'][character.parentId];
    }else{
        return aggInput['CAMPAIGN'][session.parentId];
    }
}

exports.getCharacterFromSession = function(session, aggInput){
    const PWC = session.playedWithoutCharacter;
    if(PWC == null){
        return aggInput['CHARACTER'][session.parentId];
    }else{
        return {};
    }
}


exports.buildDateFunctionString = function(date){
    return 'Date('+date.getFullYear()+', '+date.getMonth()+', '+date.getDate()+')';
}