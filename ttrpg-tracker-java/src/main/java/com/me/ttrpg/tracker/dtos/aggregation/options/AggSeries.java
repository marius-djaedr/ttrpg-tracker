package com.me.ttrpg.tracker.dtos.aggregation.options;

public class AggSeries extends AbstractAggChild<AggChartOptions> {
	private Integer targetAxisIndex;

	AggSeries(final AggChartOptions parent) {
		super(parent);
	}

	public AggSeries withTargetAxisIndex(final Integer targetAxisIndex) {
		this.targetAxisIndex = targetAxisIndex;
		return this;
	}

}
