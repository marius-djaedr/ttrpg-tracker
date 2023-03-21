package com.me.ttrpg.tracker.aggregators.completion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.me.ttrpg.tracker.aggregators.AbstractGraphAggregator;
import com.me.ttrpg.tracker.dtos.aggregation.AggOutputDto;
import com.me.ttrpg.tracker.dtos.aggregation.CampaignCompletionPojo;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.utils.AggregationUtils;

public abstract class CampaignCompletionAbstractAggregator extends AbstractGraphAggregator {
	private static final BigDecimal MAX_ONE_SHOT = new BigDecimal("2.00");
	private static final BigDecimal MAX_SHORT = new BigDecimal("6.00");
	private static final BigDecimal MAX_MID = new BigDecimal("13.00");
	private static final BigDecimal MAX_LONG = new BigDecimal("30.00");

	private Map<String, BigDecimal> currentCampaigns;
	private Map<String, BigDecimal> abandonedCampaigns;
	private Map<String, BigDecimal> completedOneShotCampaigns;
	private Map<String, BigDecimal> completedShortCampaigns;
	private Map<String, BigDecimal> completedMidCampaigns;
	private Map<String, BigDecimal> completedLongCampaigns;
	private Map<String, BigDecimal> completedExtraLongCampaigns;

	@Override
	public void initialize() {
		currentCampaigns = new HashMap<>();
		abandonedCampaigns = new HashMap<>();
		completedOneShotCampaigns = new HashMap<>();
		completedShortCampaigns = new HashMap<>();
		completedMidCampaigns = new HashMap<>();
		completedLongCampaigns = new HashMap<>();
		completedExtraLongCampaigns = new HashMap<>();
	}

	@Override
	public void processCampaign(final TtrpgCampaign element) {
		final String key = name(element);
		final Boolean completed = element.getCompletedFlg();
		if(completed == null) {
			increment(key, currentCampaigns);
		} else if(!completed) {
			increment(key, abandonedCampaigns);
		} else {

			final BigDecimal totalNumSessions = AggregationUtils.numberSessions(element);
			if(MAX_LONG.compareTo(totalNumSessions) < 0) {
				increment(key, completedExtraLongCampaigns);
			} else if(MAX_MID.compareTo(totalNumSessions) < 0) {
				increment(key, completedLongCampaigns);
			} else if(MAX_SHORT.compareTo(totalNumSessions) < 0) {
				increment(key, completedMidCampaigns);
			} else if(MAX_ONE_SHOT.compareTo(totalNumSessions) < 0) {
				increment(key, completedShortCampaigns);
			} else {
				increment(key, completedOneShotCampaigns);
			}
		}
	}

	private void increment(final String key, final Map<String, BigDecimal> map) {
		AggregationUtils.increment(map, key);
	}

	@Override
	protected List<AggOutputDto<?>> complete() {
		final AggOutputDto<CampaignCompletionPojo> campaignChart = new AggOutputDto<>("Campaign Completion - " + titleType(), "corechart", "BarChart",
				CampaignCompletionPojo.class, "name", "abandoned", "completedOneShot", "completedShort", "completedMid", "completedLong",
				"completedExtraLong", "current");

		campaignChart.getOptions().withWidth(1000).withHeight(600).withIsStacked(true).inVAxis().inTextStyle().withFontSize(10).done().done()
				.inChartArea().withTop(50);

		final Set<String> nameSet = new HashSet<>();
		nameSet.addAll(currentCampaigns.keySet());
		nameSet.addAll(abandonedCampaigns.keySet());
		nameSet.addAll(completedOneShotCampaigns.keySet());
		nameSet.addAll(completedShortCampaigns.keySet());
		nameSet.addAll(completedMidCampaigns.keySet());
		nameSet.addAll(completedLongCampaigns.keySet());
		nameSet.addAll(completedExtraLongCampaigns.keySet());

		final List<String> nameList = new ArrayList<>(nameSet);
		nameList.sort(String::compareTo);

		for(final String name : nameList) {
			final BigDecimal numberCurrent = currentCampaigns.getOrDefault(name, BigDecimal.ZERO);
			final BigDecimal numberAbandoned = abandonedCampaigns.getOrDefault(name, BigDecimal.ZERO);
			final BigDecimal numberCompletedOneShot = completedOneShotCampaigns.getOrDefault(name, BigDecimal.ZERO);
			final BigDecimal numberCompletedShort = completedShortCampaigns.getOrDefault(name, BigDecimal.ZERO);
			final BigDecimal numberCompletedMid = completedMidCampaigns.getOrDefault(name, BigDecimal.ZERO);
			final BigDecimal numberCompletedLong = completedLongCampaigns.getOrDefault(name, BigDecimal.ZERO);
			final BigDecimal numberCompletedExtraLong = completedExtraLongCampaigns.getOrDefault(name, BigDecimal.ZERO);

			final CampaignCompletionPojo pojo = new CampaignCompletionPojo();
			pojo.setName(name);
			pojo.setCurrent(numberCurrent);
			pojo.setAbandoned(numberAbandoned);
			pojo.setCompletedOneShot(numberCompletedOneShot);
			pojo.setCompletedShort(numberCompletedShort);
			pojo.setCompletedMid(numberCompletedMid);
			pojo.setCompletedLong(numberCompletedLong);
			pojo.setCompletedExtraLong(numberCompletedExtraLong);

			campaignChart.addRow(pojo);
		}
		return Arrays.asList(campaignChart);
	}

	protected abstract String name(TtrpgCampaign campaign);

	protected abstract String titleType();

}
