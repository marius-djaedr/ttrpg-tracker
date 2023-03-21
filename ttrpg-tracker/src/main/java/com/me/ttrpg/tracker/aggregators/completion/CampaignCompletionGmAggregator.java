package com.me.ttrpg.tracker.aggregators.completion;

import org.springframework.stereotype.Component;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;

@Component
public class CampaignCompletionGmAggregator extends CampaignCompletionAbstractAggregator {

	@Override
	protected String name(final TtrpgCampaign campaign) {
		return campaign.getGm();
	}

	@Override
	protected String titleType() {
		return "GM";
	}

}
