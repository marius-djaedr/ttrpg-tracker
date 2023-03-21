package com.me.ttrpg.tracker.dtos.aggregation.options;

import java.util.HashMap;
import java.util.Map;

public class AggChartOptions extends AbstractAggChild<AggChartOptions> {
	private final String title;
	private Integer width;
	private Integer height;
	private Boolean isStacked;
	private AggChartOptions chart;
	private AggAxis vAxis;
	private AggChartArea chartArea;
	private AggLegend legend;
	private AggExplorer explorer;
	private Map<Integer, AggSeries> series;
	private Map<Integer, AggAxis> vAxes;

	public AggChartOptions(final String title) {
		super(null);
		this.title = title;
	}

	private AggChartOptions(final AggChartOptions parent) {
		super(parent);
		title = null;
	}

	public String getTitle() {
		return title;
	}

	public AggChartOptions withWidth(final Integer width) {
		this.width = width;
		return this;
	}

	public AggChartOptions withHeight(final Integer height) {
		this.height = height;
		return this;
	}

	public AggChartOptions withIsStacked(final Boolean isStacked) {
		this.isStacked = isStacked;
		return this;
	}

	public AggChartOptions inChart() {
		if(chart == null) {
			chart = new AggChartOptions(this);
		}
		return chart;
	}

	public AggAxis inVAxis() {
		if(vAxis == null) {
			vAxis = new AggAxis(this);
		}
		return vAxis;
	}

	public AggChartArea inChartArea() {
		if(chartArea == null) {
			chartArea = new AggChartArea(this);
		}
		return chartArea;
	}

	public AggLegend inLegend() {
		if(legend == null) {
			legend = new AggLegend(this);
		}
		return legend;
	}

	public AggExplorer inExplorer() {
		if(explorer == null) {
			explorer = new AggExplorer(this);
		}
		return explorer;
	}

	public AggSeries inSeries(final int index) {
		if(series == null) {
			series = new HashMap<>();
		}
		if(series.containsKey(index)) {
			return series.get(index);
		}
		final AggSeries internalSeries = new AggSeries(this);
		series.put(index, internalSeries);
		return internalSeries;
	}

	public AggAxis inVAxes(final int index) {
		if(vAxes == null) {
			vAxes = new HashMap<>();
		}
		if(vAxes.containsKey(index)) {
			return vAxes.get(index);
		}
		final AggAxis internalVAxes = new AggAxis(this);
		vAxes.put(index, internalVAxes);
		return internalVAxes;
	}
}
