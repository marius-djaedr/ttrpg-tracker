package com.me.ttrpg.tracker.aggregators.timebar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import com.me.ttrpg.tracker.aggregators.AbstractGraphAggregator;
import com.me.ttrpg.tracker.dtos.aggregation.AggOutputDto;
import com.me.ttrpg.tracker.dtos.aggregation.TimeBarAggPojo;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;
import com.me.ttrpg.tracker.dtos.entities.TtrpgSession;

public abstract class AbstractTimeBarAggregator<R, B>extends AbstractGraphAggregator {
	private static final List<String> COLOR_ORDER = Arrays.asList("#6E3E98", "#A2CA7D", "#98413E", "#7DC8CA", "#A67DCA", "#69983E", "#CA807D",
			"#3E9598");

	private static final DateTimeFormatter TOOLTIP_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

	protected static final Function<TtrpgCampaign, String> SYS_FUNC = TtrpgCampaign::getSystem;
	protected static final Function<TtrpgCampaign, String> GM_FUNC = TtrpgCampaign::getGm;
	protected static final Function<TtrpgCampaign, String> NAME_FUNC = TtrpgCampaign::getName;
	protected static final Function<TtrpgCharacter, String> CHA_NAME_FUNC = TtrpgCharacter::getName;

	private List<InternalPojo> rowBarAggs;

	@Override
	public void initialize() {
		rowBarAggs = new ArrayList<>();
		addToList(rowBarAggs);
	}

	@Override
	public void processCampaign(final TtrpgCampaign element) {
		rStream(element).forEach(r -> bStream(r).forEach(b -> rowBarAggs.forEach(a -> a.processElement(r, b))));
	}

	@Override
	protected List<AggOutputDto<?>> complete() {
		final List<AggOutputDto<?>> list = new ArrayList<>();

		for(final InternalPojo rowBarAgg : rowBarAggs) {
			AggOutputDto<TimeBarAggPojo> chart;
			if(rowBarAgg.isCategorized()) {
				chart = new AggOutputDto<>("Time Bar - " + rowBarAgg.chartName(), "timeline", "Timeline", TimeBarAggPojo.class, "rowLabel",
						"barLabel", "tooltip", "color", "startDate", "endDate");
			} else {
				chart = new AggOutputDto<>("Time Bar - " + rowBarAgg.chartName(), "timeline", "Timeline", TimeBarAggPojo.class, "rowLabel",
						"barLabel", "startDate", "endDate");
			}
			chart.getOptions().withHeight(800);
			commonBuild(rowBarAgg, chart);
			list.add(chart);
		}
		return list;
	}

	private void commonBuild(final InternalPojo agg, final AggOutputDto<TimeBarAggPojo> chart) {
		final List<TimeBarAggPojo> bars = agg.finalizeAndGetList();
		bars.forEach(chart::addRow);

	}

	protected abstract void addToList(List<InternalPojo> aggList);

	protected abstract Stream<R> rStream(TtrpgCampaign campaign);

	protected abstract Stream<B> bStream(R rowElement);

	protected abstract Stream<TtrpgSession> sessionStream(B barElement);

	protected class InternalPojo {

		private final String rowName, barName, categoryName;
		private final boolean categorized;
		private final Function<R, String> rowFunc;
		private final Function<B, String> barFunc;
		private final Function<B, String> categoryFunc;
		private final Map<Triple<String, String, String>, List<TimelinePeriod>> row_bar_category_x_timelineMap = new HashMap<>();
		private final Map<String, Integer> categoryCountMap = new HashMap<>();

		protected InternalPojo(final String rowName, final Function<R, String> rowFunc, final String barName, final Function<B, String> barFunc) {
			this.rowName = rowName;
			this.rowFunc = rowFunc;

			this.barName = barName;
			this.barFunc = barFunc;

			this.categoryName = null;
			this.categoryFunc = c -> null;
			categorized = false;
		}

		protected InternalPojo(final String rowName, final Function<R, String> rowFunc, final String barName, final Function<B, String> barFunc,
				final String categoryName, final Function<B, String> categoryFunc) {
			this.rowName = rowName;
			this.rowFunc = rowFunc;

			this.barName = barName;
			this.barFunc = barFunc;

			this.categoryName = categoryName;
			this.categoryFunc = categoryFunc;
			categorized = true;
		}

		private void processElement(final R rowElement, final B barElement) {
			final String leftToSplit = rowFunc.apply(rowElement);
			final String middleToSplit = barFunc.apply(barElement);
			final String right = categoryFunc.apply(barElement);

			final Integer categoryCount = categoryCountMap.getOrDefault(right, 0);
			categoryCountMap.put(right, categoryCount + 1);

			final List<String> leftList = leftToSplit == null ? Collections.singletonList(null) : Arrays.asList(leftToSplit.split("/"));
			final List<String> middleList = middleToSplit == null ? Collections.singletonList(null) : Arrays.asList(middleToSplit.split("/"));
			for(final String left : leftList) {
				for(final String middle : middleList) {
					final Triple<String, String, String> key = Triple.of(left, middle, right);
					final List<TimelinePeriod> sessions = row_bar_category_x_timelineMap.getOrDefault(key, new ArrayList<>());
					row_bar_category_x_timelineMap.put(key, sessions);

					sessionStream(barElement).map(TtrpgSession::getPlayDate).map(TimelinePeriod::new).forEach(sessions::add);
				}
			}
		}

		private boolean isCategorized() {
			return categorized;
		}

		private List<TimeBarAggPojo> finalizeAndGetList() {
			for(final Triple<String, String, String> key : row_bar_category_x_timelineMap.keySet()) {
				final List<TimelinePeriod> consolidated = consolidatePeriods(row_bar_category_x_timelineMap.get(key));
				row_bar_category_x_timelineMap.put(key, consolidated);
			}

			final Map<String, String> categoryColorMap = buildCategoryColorMap();

			final Map<String, List<TimeBarAggPojo>> rowMap = new HashMap<>();

			for(final Map.Entry<Triple<String, String, String>, List<TimelinePeriod>> superEntry : row_bar_category_x_timelineMap.entrySet()) {
				final String row = superEntry.getKey().getLeft();
				final List<TimeBarAggPojo> bars = rowMap.getOrDefault(row, new ArrayList<>());
				rowMap.put(row, bars);

				//TODO do I need to sort the bar names?

				final String bar = String.valueOf(superEntry.getKey().getMiddle());
				final String category = superEntry.getKey().getRight();
				final String color = categoryColorMap.get(category);

				for(final TimelinePeriod tp : superEntry.getValue()) {
					final TimeBarAggPojo stPojo = new TimeBarAggPojo();
					stPojo.setRowLabel(row);
					stPojo.setBarLabel(bar);
					final String tooltip = buildTooltip(row, bar, category, tp);
					stPojo.setTooltip(tooltip);
					stPojo.setColor(color);
					stPojo.setStartDate(tp.getStartDate());
					stPojo.setEndDate(tp.getEndDate());
					bars.add(stPojo);
				}

			}

			return rowMap.entrySet().stream().sorted((e1, e2) -> StringUtils.compare(e1.getKey(), e2.getKey())).map(Map.Entry::getValue)
					.flatMap(List::stream).collect(Collectors.toList());

		}

		private Map<String, String> buildCategoryColorMap() {

			final List<String> orderedCategories = categoryCountMap.entrySet().stream()
					.sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
			final Map<String, String> categoryColorMap = new HashMap<>();
			for(int i = 0 ; i < orderedCategories.size() ; i++) {
				categoryColorMap.put(orderedCategories.get(i), COLOR_ORDER.get(i));
			}
			return categoryColorMap;
		}

		private List<TimelinePeriod> consolidatePeriods(final List<TimelinePeriod> listIn) {
			if(listIn.isEmpty()) {
				return new ArrayList<>();
			}
			listIn.sort((tp1, tp2) -> tp1.startDate.compareTo(tp2.startDate));

			final List<TimelinePeriod> listOut = new ArrayList<>();

			TimelinePeriod currentPeriod = listIn.get(0);
			listOut.add(currentPeriod);

			for(int i = 1 ; i < listIn.size() ; i++) {
				final TimelinePeriod nextPeriod = listIn.get(i);
				if(currentPeriod.containsOrNearBoundary(nextPeriod.startDate)) {
					currentPeriod.endDate = nextPeriod.endDate;
				} else {
					listOut.add(nextPeriod);
					currentPeriod = nextPeriod;
				}
			}

			return listOut;
		}

		private String chartName() {
			String chartName = rowName + " - " + barName;
			if(categorized) {
				chartName += " - " + categoryName;
			}
			return chartName;
		}

		private String buildTooltip(final String row, final String bar, final String category, final TimelinePeriod tp) {

			//@formatter:off
			final String tooltip=
"<ul class=\"google-visualization-tooltip-item-list\">"+
  "<li class=\"google-visualization-tooltip-item\">"+
    "<span style=\"font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:bold;\">"+
      bar+
    "</span>"+
  "</li>"+
"</ul>"+
"<div class=\"google-visualization-tooltip-separator\"></div>"+
"<ul class=\"google-visualization-tooltip-action-list\">"+
  "<li data-logicalname=\"action#\" class=\"google-visualization-tooltip-action\">"+
    "<span style=\"font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:bold;\">"+
      row+": "+
    "</span>"+
    "<span style=\"font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:none;\">"+
      tp.startDate.format(TOOLTIP_FORMAT)+" - "+tp.endDate.format(TOOLTIP_FORMAT)+
    "</span>"+
  "</li>"+
  "<li data-logicalname=\"action#\" class=\"google-visualization-tooltip-action\">"+
    "<span style=\"font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:bold;\">"+
      categoryName+": "+
    "</span>"+
    "<span style=\"font-family:Arial;font-size:12px;color:#000000;opacity:1;margin:0;font-style:none;text-decoration:none;font-weight:none;\">"+
      category+
    "</span>"+
  "</li>"+
"</ul>";

				//@formatter:on

			return tooltip;
		}

	}

	private class TimelinePeriod {
		private static final long BUFFER = 35L;

		private LocalDate startDate;
		private LocalDate endDate;

		private LocalDate getStartDate() {
			return startDate;
		}

		private LocalDate getEndDate() {
			return endDate;
		}

		private TimelinePeriod(final LocalDate playDate) {
			startDate = playDate;

			endDate = playDate.plusDays(1L);
		}

		private boolean containsOrNearBoundary(final LocalDate playDate) {

			if(!startDate.isAfter(playDate) && !endDate.isBefore(playDate)) {
				//already accounted for
				return true;
			} else if(startDate.isAfter(playDate) && !startDate.minusDays(BUFFER).isAfter(playDate)) {
				startDate = playDate;
				return true;
			} else if(endDate.isBefore(playDate) && !endDate.plusDays(BUFFER).isBefore(playDate)) {
				endDate = playDate;
				return true;
			} else {
				return false;
			}
		}

	}
}
