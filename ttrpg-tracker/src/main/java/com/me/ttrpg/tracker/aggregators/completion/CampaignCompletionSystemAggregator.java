package com.me.ttrpg.tracker.aggregators.completion;

import org.springframework.stereotype.Component;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;

@Component
public class CampaignCompletionSystemAggregator extends CampaignCompletionAbstractAggregator {

	@Override
	protected String name(final TtrpgCampaign campaign) {
		return campaign.getSystem();
	}

	@Override
	protected String titleType() {
		return "System";
	}

}
