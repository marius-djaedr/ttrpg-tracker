package com.me.ttrpg.tracker.dtos.aggregation.options;

import java.math.BigDecimal;

public class AggAxis extends AbstractAggChild<AggChartOptions> {
	private String title;
	private BigDecimal minValue;
	private BigDecimal maxValue;
	private AggAxisTextStyle textStyle;

	AggAxis(final AggChartOptions parent) {
		super(parent);
	}

	public AggAxis withTitle(final String title) {
		this.title = title;
		return this;
	}

	public AggAxis withMinValue(final BigDecimal minValue) {
		this.minValue = minValue;
		return this;
	}

	public AggAxis withMaxValue(final BigDecimal maxValue) {
		this.maxValue = maxValue;
		return this;
	}

	public AggAxisTextStyle inTextStyle() {
		if(textStyle == null) {
			textStyle = new AggAxisTextStyle(this);
		}
		return textStyle;
	}
}
