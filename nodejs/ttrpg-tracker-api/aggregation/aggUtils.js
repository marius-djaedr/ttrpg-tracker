
export function convertBooleanToWord(bool) {
    if(bool == null){
        return 'Unknown';
    }else if(bool || bool == 'true'){
        return 'Yes';
    }else{
        return 'No';
    }
}

export function getCampaignFromSession(session, aggInput){
    const PWC = session.playedWithoutCharacter;
    if(PWC == null){
        const character = aggInput['CHARACTER'][session.parentId];
        return aggInput['CAMPAIGN'][character.parentId];
    }else{
        return aggInput['CAMPAIGN'][session.parentId];
    }
}