
exports.convertBooleanToWord = function(bool) {
    if(bool == null || bool === ''){
        return 'Unknown';
    }else if(bool || bool == 'true'){
        return 'Yes';
    }else{
        return 'No';
    }
}

exports.getCampaignFromSession = function(session, aggInput){
    const PWC = session.playedWithoutCharacter;
    if(PWC == null || PWC === ''){
        const character = aggInput['CHARACTER'][session.parentId];
        return aggInput['CAMPAIGN'][character.parentId];
    }else{
        return aggInput['CAMPAIGN'][session.parentId];
    }
}

exports.getCharacterFromSession = function(session, aggInput){
    const PWC = session.playedWithoutCharacter;
    if(PWC == null || PWC === ''){
        return aggInput['CHARACTER'][session.parentId];
    }else{
        return {};
    }
}


exports.buildDateFunctionString = function(date){
    return 'Date('+date.getFullYear()+', '+date.getMonth()+', '+date.getDate()+')';
}

exports.getSessionWeightedCount = function(sessions){
    let count = 0.0;
    for(const session of sessions){
        if(session.shortSession==null || session.shortSession==='' || session.shortSession === 'false' || session.shortSession === false){
            count += 1.0;
        }else{
            count += 0.5;
        }
    }
    return count;
}