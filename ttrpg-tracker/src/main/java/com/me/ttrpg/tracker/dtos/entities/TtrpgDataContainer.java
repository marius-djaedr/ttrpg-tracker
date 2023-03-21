package com.me.ttrpg.tracker.dtos.entities;

import java.util.Map;

public class TtrpgDataContainer {
	private Map<String, TtrpgCampaign> campaignMap;

	public Map<String, TtrpgCampaign> getCampaignMap() {
		return campaignMap;
	}

	public void setCampaignMap(final Map<String, TtrpgCampaign> campaignMap) {
		this.campaignMap = campaignMap;
	}
}
