package com.me.ttrpg.tracker.aggregators.completion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.me.ttrpg.tracker.aggregators.AbstractGraphAggregator;
import com.me.ttrpg.tracker.dtos.aggregation.AggOutputDto;
import com.me.ttrpg.tracker.dtos.aggregation.CampaignCompletionPojo;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.utils.AggregationUtils;

@Component
public class CampaignGranulatedCompletionAggregator extends AbstractGraphAggregator {

	private Map<BigDecimal, BigDecimal> currentCampaigns;
	private Map<BigDecimal, BigDecimal> abandonedCampaigns;
	private Map<BigDecimal, BigDecimal> completedCampaigns;

	@Override
	public void initialize() {
		currentCampaigns = new HashMap<>();
		abandonedCampaigns = new HashMap<>();
		completedCampaigns = new HashMap<>();
	}

	@Override
	public void processCampaign(final TtrpgCampaign element) {
		final BigDecimal totalNumSessions = AggregationUtils.numberSessions(element);
		final Boolean completed = element.getCompletedFlg();
		if(completed == null) {
			AggregationUtils.increment(currentCampaigns, totalNumSessions);
		} else if(!completed) {
			AggregationUtils.increment(abandonedCampaigns, totalNumSessions);
		} else {
			AggregationUtils.increment(completedCampaigns, totalNumSessions);
		}
	}

	@Override
	protected List<AggOutputDto<?>> complete() {
		final AggOutputDto<CampaignCompletionPojo> campaignChart = new AggOutputDto<>("Campaign Granulated Completion", "corechart", "BarChart",
				CampaignCompletionPojo.class, "name", "abandoned", "completed", "current");

		campaignChart.getOptions().withWidth(1000).withHeight(600).withIsStacked(true).inVAxis().inTextStyle().withFontSize(10).done().done()
				.inChartArea().withTop(50);

		final Set<BigDecimal> nameSet = new HashSet<>();
		nameSet.addAll(currentCampaigns.keySet());
		nameSet.addAll(abandonedCampaigns.keySet());
		nameSet.addAll(completedCampaigns.keySet());

		final List<BigDecimal> nameList = new ArrayList<>(nameSet);
		nameList.sort(BigDecimal::compareTo);

		for(final BigDecimal name : nameList) {
			final BigDecimal numberCurrent = currentCampaigns.getOrDefault(name, BigDecimal.ZERO);
			final BigDecimal numberAbandoned = abandonedCampaigns.getOrDefault(name, BigDecimal.ZERO);
			final BigDecimal numberCompleted = completedCampaigns.getOrDefault(name, BigDecimal.ZERO);

			final CampaignCompletionPojo pojo = new CampaignCompletionPojo();
			pojo.setName(name.toPlainString());
			pojo.setCurrent(numberCurrent);
			pojo.setAbandoned(numberAbandoned);
			pojo.setCompleted(numberCompleted);

			campaignChart.addRow(pojo);
		}
		return Arrays.asList(campaignChart);
	}

}
