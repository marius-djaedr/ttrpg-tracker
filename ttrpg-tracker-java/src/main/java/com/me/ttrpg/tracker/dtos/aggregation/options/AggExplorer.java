package com.me.ttrpg.tracker.dtos.aggregation.options;

public class AggExplorer extends AbstractAggChild<AggChartOptions> {
	private String axis;

	AggExplorer(final AggChartOptions parent) {
		super(parent);
	}

	public AggExplorer withAxis(final String axis) {
		this.axis = axis;
		return this;
	}

}
