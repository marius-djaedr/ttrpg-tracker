package com.me.ttrpg.tracker.aggregators.count;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;

@Component
public class SystemCountAggregator extends AbstractCampaignCountAggregator {

	@Override
	protected String nameFromCampaign(final TtrpgCampaign campaign) {
		return campaign.getSystem();
	}

	@Override
	protected String title() {
		return "System";
	}

	@Override
	protected List<InternalChart> chartsToMake() {
		return Arrays.asList(InternalChart.SESSION_PIE, InternalChart.CAMPAIGN_PIE, InternalChart.SESSION_PER_CAMPAIGN);
	}
}
