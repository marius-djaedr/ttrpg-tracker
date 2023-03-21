package com.me.ttrpg.tracker.dtos.aggregation;

import java.time.LocalDate;

import com.me.ttrpg.tracker.annotations.AggColumn;

public class TimeBarAggPojo {

	@AggColumn(name = "Row Label", type = AggColumn.AggColumnType.STRING)
	private String rowLabel;

	@AggColumn(name = "Bar Label", type = AggColumn.AggColumnType.STRING)
	private String barLabel;

	@AggColumn(name = "Tooltip", type = AggColumn.AggColumnType.STRING, role = "tooltip")
	private String tooltip;

	@AggColumn(name = "style", type = AggColumn.AggColumnType.STRING, role = "style")
	private String color;

	@AggColumn(name = "Start", type = AggColumn.AggColumnType.DATE)
	private LocalDate startDate;

	@AggColumn(name = "End", type = AggColumn.AggColumnType.DATE)
	private LocalDate endDate;

	public String getRowLabel() {
		return rowLabel;
	}

	public void setRowLabel(final String rowLabel) {
		this.rowLabel = rowLabel;
	}

	public String getBarLabel() {
		return barLabel;
	}

	public void setBarLabel(final String barLabel) {
		this.barLabel = barLabel;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(final String tooltip) {
		this.tooltip = tooltip;
	}

	public String getColor() {
		return color;
	}

	public void setColor(final String color) {
		this.color = color;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(final LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(final LocalDate endDate) {
		this.endDate = endDate;
	}

}
