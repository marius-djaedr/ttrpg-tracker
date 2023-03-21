package com.me.ttrpg.tracker.aggregators.count;

import java.math.BigDecimal;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.utils.AggregationUtils;

public abstract class AbstractCampaignCountAggregator extends AbstractCountAggregator {

	@Override
	public void processCampaign(final TtrpgCampaign element) {
		final String key = nameFromCampaign(element);
		AggregationUtils.increment(campaignMap, key);

		final BigDecimal sessions = AggregationUtils.numberSessions(element);
		AggregationUtils.increment(sessionMap, key, sessions);
	}

	protected abstract String nameFromCampaign(TtrpgCampaign campaign);

}
