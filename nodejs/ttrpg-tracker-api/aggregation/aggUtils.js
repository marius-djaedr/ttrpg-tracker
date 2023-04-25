
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

}