package com.me.ttrpg.tracker.dtos.aggregation.options;

public class AggAxisTextStyle extends AbstractAggChild<AggAxis> {
	private Integer fontSize;

	AggAxisTextStyle(final AggAxis parent) {
		super(parent);
	}

	public AggAxisTextStyle withFontSize(final Integer fontSize) {
		this.fontSize = fontSize;
		return this;
	}
}
