const AggUtils = require('../aggUtils');

module.exports = function() {

    this.gmCampaignCompleted = require('../reference/timeBar')({
        chartName: 'GM - Campaign - Completed',
        categoryName: 'Completed',
        session_rowNameFunc: (session, aggInput) => AggUtils.getCampaignFromSession(session, aggInput).gm,
        session_barNameFunc: (session, aggInput) => AggUtils.getCampaignFromSession(session, aggInput).name,
        session_categoryFunc: (session, aggInput) => AggUtils.convertBooleanToWord(AggUtils.getCampaignFromSession(session, aggInput).completed),
    });

    this.aggregate = async function(aggInput) {
        return [gmCampaignCompleted.processAll(aggInput)];
    }
}