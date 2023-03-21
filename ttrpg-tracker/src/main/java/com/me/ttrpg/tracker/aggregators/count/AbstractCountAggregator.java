package com.me.ttrpg.tracker.aggregators.count;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.me.ttrpg.tracker.aggregators.AbstractGraphAggregator;
import com.me.ttrpg.tracker.dtos.aggregation.AggOutputDto;
import com.me.ttrpg.tracker.dtos.aggregation.CountAggPojo;
import com.me.ttrpg.tracker.dtos.aggregation.options.AggChartOptions;

public abstract class AbstractCountAggregator extends AbstractGraphAggregator {

	protected Map<String, BigDecimal> sessionMap;
	protected Map<String, BigDecimal> characterMap;
	protected Map<String, BigDecimal> campaignMap;

	@Override
	public void initialize() {
		sessionMap = new HashMap<>();
		characterMap = new HashMap<>();
		campaignMap = new HashMap<>();
	}

	@Override
	protected List<AggOutputDto<?>> complete() {
		final String title = title();

		final List<AggOutputDto<CountAggPojo>> charts = chartsToMake().stream().map(e -> e.constructorWithOptions.apply(title))
				.collect(Collectors.toList());

		final Set<String> nameSet = new HashSet<>();
		nameSet.addAll(sessionMap.keySet());
		nameSet.addAll(characterMap.keySet());
		nameSet.addAll(campaignMap.keySet());
		nameSet.remove(null);

		final List<String> nameList = new ArrayList<>(nameSet);
		nameList.sort(String::compareTo);

		for(final String name : nameList) {
			final BigDecimal numberCampaigns = campaignMap.getOrDefault(name, BigDecimal.ZERO);
			final BigDecimal numberCharacters = characterMap.getOrDefault(name, BigDecimal.ZERO);
			final BigDecimal numberSessions = sessionMap.getOrDefault(name, BigDecimal.ZERO);

			BigDecimal sessionsPerCampaign;
			if(BigDecimal.ZERO.compareTo(numberCampaigns) == 0) {
				sessionsPerCampaign = null;
			} else {
				sessionsPerCampaign = numberSessions.setScale(10).divide(numberCampaigns.setScale(10), RoundingMode.HALF_EVEN);
			}

			final CountAggPojo pojo = new CountAggPojo();
			pojo.setName(name);
			pojo.setSessions(numberSessions);
			pojo.setCharacters(numberCharacters);
			pojo.setCampaigns(numberCampaigns);
			pojo.setSessionsPerCampaign(sessionsPerCampaign);

			charts.forEach(c -> c.addRow(pojo));
		}
		return new ArrayList<>(charts);
	}

	protected List<InternalChart> chartsToMake() {
		return Arrays.asList(InternalChart.values());
	}

	protected abstract String title();

	protected enum InternalChart {
		SESSION_PIE(t -> new AggOutputDto<>("Sessions (Pie) - " + t, "corechart", "PieChart", CountAggPojo.class, "name", "sessions"),
				o -> o.withWidth(1000).withHeight(600).inChartArea().withTop(50).withHeight(400).withBottom(0).done().inLegend()
						.withAlignment("center").done()),

		SESSION_BAR(t -> new AggOutputDto<>("Sessions (Bar) - " + t, "corechart", "BarChart", CountAggPojo.class, "name", "sessions"),
				o -> o.withWidth(1200).withHeight(2000).inVAxis().inTextStyle().withFontSize(10).done().done().inChartArea().withTop(50)),

		CHARACTER_PIE(t -> new AggOutputDto<>("Characters (Pie) - " + t, "corechart", "PieChart", CountAggPojo.class, "name", "characters"),
				o -> o.withWidth(1000).withHeight(600).inChartArea().withTop(50).withHeight(400).withBottom(0).done().inLegend()
						.withAlignment("center")),

		CHARACTER_BAR(t -> new AggOutputDto<>("Characters (Bar) - " + t, "corechart", "BarChart", CountAggPojo.class, "name", "characters"),
				o -> o.withWidth(1200).withHeight(2000).inVAxis().inTextStyle().withFontSize(10).done().done().inChartArea().withTop(50)),

		CAMPAIGN_PIE(t -> new AggOutputDto<>("Campaigns (Pie) - " + t, "corechart", "PieChart", CountAggPojo.class, "name", "campaigns"),
				o -> o.withWidth(1000).withHeight(600).inChartArea().withTop(50).withHeight(400).withBottom(0).done().inLegend()
						.withAlignment("center")),

		CAMPAIGN_BAR(t -> new AggOutputDto<>("Campaigns (Bar) - " + t, "corechart", "BarChart", CountAggPojo.class, "name", "campaigns"),
				o -> o.withWidth(1200).withHeight(2000).inVAxis().inTextStyle().withFontSize(10).done().done().inChartArea().withTop(50)),

		SESSION_PER_CAMPAIGN(
				t -> new AggOutputDto<>("Sessions Per Campaign - " + t, "corechart", "BarChart", CountAggPojo.class, "name", "sessionsPerCampaign"),
				o -> o.withWidth(700).withHeight(600).inVAxis().inTextStyle().withFontSize(10).done().done().inChartArea().withTop(50))

		;

		private InternalChart(final Function<String, AggOutputDto<CountAggPojo>> constructor, final Consumer<AggChartOptions> options) {
			constructorWithOptions = t -> {
				final AggOutputDto<CountAggPojo> dto = constructor.apply(t);
				options.accept(dto.getOptions());
				return dto;
			};
		}

		private final Function<String, AggOutputDto<CountAggPojo>> constructorWithOptions;

	}
}
