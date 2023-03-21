package com.me.ttrpg.tracker.aggregators.count;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;

@Component
public class CampaignCountAggregator extends AbstractCampaignCountAggregator {

	@Override
	protected String nameFromCampaign(final TtrpgCampaign campaign) {
		return campaign.getGm() + "-" + campaign.getName() + " (" + campaign.getSystem() + ")";
	}

	@Override
	protected String title() {
		return "Campaign";
	}

	@Override
	protected List<InternalChart> chartsToMake() {
		return Arrays.asList(InternalChart.SESSION_BAR);
	}
}
