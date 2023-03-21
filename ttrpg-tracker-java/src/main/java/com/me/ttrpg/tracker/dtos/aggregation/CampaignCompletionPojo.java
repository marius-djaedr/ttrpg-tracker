package com.me.ttrpg.tracker.dtos.aggregation;

import java.math.BigDecimal;

import com.me.ttrpg.tracker.annotations.AggColumn;

public class CampaignCompletionPojo {

	@AggColumn(name = "Name")
	private String name;

	@AggColumn(name = "Abandoned Campaigns", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal abandoned;

	@AggColumn(name = "Completed One-Shot Campaigns (1-2)", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal completedOneShot;

	@AggColumn(name = "Completed Short Campaigns (3-6)", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal completedShort;

	@AggColumn(name = "Completed Mid Campaigns (7-13)", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal completedMid;

	@AggColumn(name = "Completed Long Campaigns (14-20)", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal completedLong;

	@AggColumn(name = "Completed Extra Long Campaigns (21+)", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal completedExtraLong;

	@AggColumn(name = "Current Campaigns", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal current;

	@AggColumn(name = "Completed", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal completed;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public BigDecimal getAbandoned() {
		return abandoned;
	}

	public void setAbandoned(final BigDecimal abandoned) {
		this.abandoned = abandoned;
	}

	public BigDecimal getCompletedOneShot() {
		return completedOneShot;
	}

	public void setCompletedOneShot(final BigDecimal completedOneShot) {
		this.completedOneShot = completedOneShot;
	}

	public BigDecimal getCompletedShort() {
		return completedShort;
	}

	public void setCompletedShort(final BigDecimal completedShort) {
		this.completedShort = completedShort;
	}

	public BigDecimal getCompletedMid() {
		return completedMid;
	}

	public void setCompletedMid(final BigDecimal completedMid) {
		this.completedMid = completedMid;
	}

	public BigDecimal getCompletedLong() {
		return completedLong;
	}

	public void setCompletedLong(final BigDecimal completedLong) {
		this.completedLong = completedLong;
	}

	public BigDecimal getCompletedExtraLong() {
		return completedExtraLong;
	}

	public void setCompletedExtraLong(final BigDecimal completedExtraLong) {
		this.completedExtraLong = completedExtraLong;
	}

	public BigDecimal getCurrent() {
		return current;
	}

	public void setCurrent(final BigDecimal current) {
		this.current = current;
	}

	public BigDecimal getCompleted() {
		return completed;
	}

	public void setCompleted(final BigDecimal completed) {
		this.completed = completed;
	}
}
