package com.me.ttrpg.tracker.dtos.aggregation.options;

public class AggLegend extends AbstractAggChild<AggChartOptions> {
	private String alignment;

	AggLegend(final AggChartOptions parent) {
		super(parent);
	}

	public AggLegend withAlignment(final String alignment) {
		this.alignment = alignment;
		return this;
	}

}
