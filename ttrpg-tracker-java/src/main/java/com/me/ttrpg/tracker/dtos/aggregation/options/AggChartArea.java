package com.me.ttrpg.tracker.dtos.aggregation.options;

public class AggChartArea extends AbstractAggChild<AggChartOptions> {
	private Integer top;
	private Integer height;
	private Integer bottom;

	AggChartArea(final AggChartOptions parent) {
		super(parent);
	}

	public AggChartArea withTop(final Integer top) {
		this.top = top;
		return this;
	}

	public AggChartArea withHeight(final Integer height) {
		this.height = height;
		return this;
	}

	public AggChartArea withBottom(final Integer bottom) {
		this.bottom = bottom;
		return this;
	}

}
