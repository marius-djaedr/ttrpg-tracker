package com.me.ttrpg.tracker.dtos.aggregation;

import java.math.BigDecimal;

import com.me.ttrpg.tracker.annotations.AggColumn;

public class CountAggPojo {
	@AggColumn(name = "Name")
	private String name;

	@AggColumn(name = "Sessions", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal sessions;

	@AggColumn(name = "Characters", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal characters;

	@AggColumn(name = "Campaigns", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal campaigns;

	@AggColumn(name = "Sessions/Campaign", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal sessionsPerCampaign;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public BigDecimal getSessions() {
		return sessions;
	}

	public void setSessions(final BigDecimal sessions) {
		this.sessions = sessions;
	}

	public BigDecimal getCharacters() {
		return characters;
	}

	public void setCharacters(final BigDecimal characters) {
		this.characters = characters;
	}

	public BigDecimal getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(final BigDecimal campaigns) {
		this.campaigns = campaigns;
	}

	public BigDecimal getSessionsPerCampaign() {
		return sessionsPerCampaign;
	}

	public void setSessionsPerCampaign(final BigDecimal sessionsPerCampaign) {
		this.sessionsPerCampaign = sessionsPerCampaign;
	}

}
