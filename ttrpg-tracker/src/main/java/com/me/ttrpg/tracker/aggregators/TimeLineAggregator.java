package com.me.ttrpg.tracker.aggregators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.me.ttrpg.tracker.dtos.aggregation.AggOutputDto;
import com.me.ttrpg.tracker.dtos.aggregation.TimelineAggPojo;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;
import com.me.ttrpg.tracker.dtos.entities.TtrpgSession;
import com.me.ttrpg.tracker.utils.AggregationUtils;
import com.me.util.dto.HaarDecomp;
import com.me.util.dto.RightPartialHaarDecomp;

@Component
public class TimeLineAggregator extends AbstractGraphAggregator {
	private static final BigDecimal ZERO = new BigDecimal("0").setScale(10);
	private static final BigDecimal TWO = new BigDecimal("2").setScale(10);

	private static final BinaryOperator<BigDecimal> ADD_FUNC = (b1, b2) -> b1.setScale(10, RoundingMode.HALF_UP)
			.add(b2.setScale(10, RoundingMode.HALF_UP));
	private static final UnaryOperator<BigDecimal> NEGATIVE_FUNC = BigDecimal::negate;
	private static final UnaryOperator<BigDecimal> DIV_BY_2_FUNC = n -> n.divide(TWO, RoundingMode.HALF_EVEN);
	private static final Function<Integer, Integer> POW_2_SIZE_CONVERTER = (
			originalSize) -> (int) Math.pow(2., Math.ceil(Math.log(originalSize) / Math.log(2.)));

	private Map<LocalDate, BigDecimal> ranMap;
	private Map<LocalDate, BigDecimal> playedMap;

	@Override
	public void initialize() {
		ranMap = new HashMap<>();
		playedMap = new HashMap<>();
	}

	@Override
	public void processCampaign(final TtrpgCampaign element) {
		element.getCharactersIPlayed().stream().map(TtrpgCharacter::getSessionsIPlayed).flatMap(Collection::stream)
				.forEach(s -> aggregateIntoMap(playedMap, s));

		element.getSessionsIPlayedNoCharacter().stream().forEach(s -> aggregateIntoMap(playedMap, s));
		element.getSessionsIRan().stream().forEach(s -> aggregateIntoMap(ranMap, s));
	}

	private void aggregateIntoMap(final Map<LocalDate, BigDecimal> aggregateMap, final TtrpgSession session) {
		final LocalDate sessionDate = session.getPlayDate();

		final BigDecimal toAdd = AggregationUtils.sessionValue(session);

		final BigDecimal currentValue = aggregateMap.getOrDefault(sessionDate, ZERO);
		final BigDecimal newValue = currentValue.add(toAdd);
		aggregateMap.put(sessionDate, newValue);
	}

	@Override
	protected List<AggOutputDto<?>> complete() {
		final AggOutputDto<TimelineAggPojo> cumulativeChart = new AggOutputDto<>("Time Line - Cumulative", "annotationchart", "AnnotationChart",
				TimelineAggPojo.class, "date", "cumulativeRan", "cumulativePlayed", "ratio");
		cumulativeChart.getOptions().withHeight(800).inChart().inSeries(0).withTargetAxisIndex(1).done().inSeries(1).withTargetAxisIndex(1).done()
				.inSeries(2).withTargetAxisIndex(0).done().inVAxes(0).withTitle("Ratio").withMaxValue(new BigDecimal("2"))
				.withMinValue(BigDecimal.ZERO).done().inVAxes(1).withTitle("Count");

		final int firstYear = Stream.concat(ranMap.keySet().stream(), playedMap.keySet().stream()).map(LocalDate::getYear).min(Integer::compareTo)
				.get();
		final int currentYear = LocalDate.now().getYear();

		//NOTE: all of these are normalized per day
		final List<LocalDate> dateListDay = new ArrayList<>();
		final List<BigDecimal> ranListDay = new ArrayList<>();
		final List<BigDecimal> playedListDay = new ArrayList<>();
		final List<BigDecimal> totalListDay = new ArrayList<>();

		final List<LocalDate> dateListMonth = new ArrayList<>();
		final List<BigDecimal> ranListMonth = new ArrayList<>();
		final List<BigDecimal> playedListMonth = new ArrayList<>();
		final List<BigDecimal> totalListMonth = new ArrayList<>();
		final List<BigDecimal> ranDensityListMonth = new ArrayList<>();
		final List<BigDecimal> playedDensityListMonth = new ArrayList<>();
		final List<BigDecimal> totalDensityListMonth = new ArrayList<>();

		final List<LocalDate> dateListQuarter = new ArrayList<>();
		final List<BigDecimal> ranListQuarter = new ArrayList<>();
		final List<BigDecimal> playedListQuarter = new ArrayList<>();
		final List<BigDecimal> totalListQuarter = new ArrayList<>();
		final List<BigDecimal> ranDensityListQuarter = new ArrayList<>();
		final List<BigDecimal> playedDensityListQuarter = new ArrayList<>();
		final List<BigDecimal> totalDensityListQuarter = new ArrayList<>();

		final List<LocalDate> dateListYear = new ArrayList<>();
		final List<BigDecimal> ranListYear = new ArrayList<>();
		final List<BigDecimal> playedListYear = new ArrayList<>();
		final List<BigDecimal> totalListYear = new ArrayList<>();
		final List<BigDecimal> ranDensityListYear = new ArrayList<>();
		final List<BigDecimal> playedDensityListYear = new ArrayList<>();
		final List<BigDecimal> totalDensityListYear = new ArrayList<>();

		BigDecimal cumRan = BigDecimal.ZERO;
		BigDecimal cumPlayed = BigDecimal.ZERO;

		final LocalDate today = LocalDate.now();
		for(int year = firstYear ; year <= currentYear ; year++) {
			final LocalDate dateYear = LocalDate.of(year, 1, 1);
			BigDecimal numberRanYear = BigDecimal.ZERO.setScale(10);
			BigDecimal numberPlayedYear = BigDecimal.ZERO.setScale(10);
			BigDecimal numberTotalYear = BigDecimal.ZERO.setScale(10);
			int numDaysYear = 0;
			for(int q = 0 ; q < 4 ; q++) {
				final LocalDate dateQuarter = LocalDate.of(year, Month.of(q * 3 + 1), 1);
				BigDecimal numberRanQuarter = BigDecimal.ZERO.setScale(10);
				BigDecimal numberPlayedQuarter = BigDecimal.ZERO.setScale(10);
				BigDecimal numberTotalQuarter = BigDecimal.ZERO.setScale(10);
				int numDaysQuarter = 0;
				for(int m = 1 ; m <= 3 ; m++) {
					final Month month = Month.of(q * 3 + m);
					final LocalDate dateMonth = LocalDate.of(year, month, 1);
					BigDecimal numberRanMonth = BigDecimal.ZERO.setScale(10);
					BigDecimal numberPlayedMonth = BigDecimal.ZERO.setScale(10);
					BigDecimal numberTotalMonth = BigDecimal.ZERO.setScale(10);
					int numDaysMonth = 0;

					for(int day = 1 ; day <= month.length(dateYear.isLeapYear()) ; day++) {
						final LocalDate dateDay = LocalDate.of(year, month, day);
						if(!today.isBefore(dateDay)) {

							final BigDecimal numberRanDay = ranMap.getOrDefault(dateDay, BigDecimal.ZERO);
							final BigDecimal numberPlayedDay = playedMap.getOrDefault(dateDay, BigDecimal.ZERO);
							final BigDecimal numberTotalDay = numberRanDay.add(numberPlayedDay);

							dateListDay.add(dateDay);
							ranListDay.add(numberRanDay);
							playedListDay.add(numberPlayedDay);
							totalListDay.add(numberTotalDay);

							numberRanMonth = numberRanMonth.add(numberRanDay);
							numberPlayedMonth = numberPlayedMonth.add(numberPlayedDay);
							numberTotalMonth = numberTotalMonth.add(numberTotalDay);
							numDaysYear++;
							numDaysQuarter++;
							numDaysMonth++;

							//fill out cumulative chart
							cumRan = cumRan.add(numberRanDay);
							cumPlayed = cumPlayed.add(numberPlayedDay);

							final TimelineAggPojo pojo = new TimelineAggPojo();
							pojo.setDate(dateDay);
							pojo.setCumulativeRan(cumRan);
							pojo.setCumulativePlayed(cumPlayed);
							if(cumRan.compareTo(BigDecimal.ZERO) > 0) {
								pojo.setRatio(cumPlayed.setScale(10).divide(cumRan.setScale(10), RoundingMode.HALF_EVEN));
							}
							cumulativeChart.addRow(pojo);
						}
					}

					if(numDaysMonth > 0) {
						dateListMonth.add(dateMonth);

						ranListMonth.add(numberRanMonth);
						playedListMonth.add(numberPlayedMonth);
						totalListMonth.add(numberTotalMonth);

						final BigDecimal numDaysMonthBD = BigDecimal.valueOf(numDaysMonth).setScale(10);
						ranDensityListMonth.add(numberRanMonth.divide(numDaysMonthBD, RoundingMode.HALF_UP));
						playedDensityListMonth.add(numberPlayedMonth.divide(numDaysMonthBD, RoundingMode.HALF_UP));
						totalDensityListMonth.add(numberTotalMonth.divide(numDaysMonthBD, RoundingMode.HALF_UP));

						numberRanQuarter = numberRanQuarter.add(numberRanMonth);
						numberPlayedQuarter = numberPlayedQuarter.add(numberPlayedMonth);
						numberTotalQuarter = numberTotalQuarter.add(numberTotalMonth);
					}
				}
				if(numDaysQuarter > 0) {
					dateListQuarter.add(dateQuarter);

					ranListQuarter.add(numberRanQuarter);
					playedListQuarter.add(numberPlayedQuarter);
					totalListQuarter.add(numberTotalQuarter);

					final BigDecimal numDaysQuarterBD = BigDecimal.valueOf(numDaysQuarter).setScale(10);
					ranDensityListQuarter.add(numberRanQuarter.divide(numDaysQuarterBD, RoundingMode.HALF_UP));
					playedDensityListQuarter.add(numberPlayedQuarter.divide(numDaysQuarterBD, RoundingMode.HALF_UP));
					totalDensityListQuarter.add(numberTotalQuarter.divide(numDaysQuarterBD, RoundingMode.HALF_UP));

					numberRanYear = numberRanYear.add(numberRanQuarter);
					numberPlayedYear = numberPlayedYear.add(numberPlayedQuarter);
					numberTotalYear = numberTotalYear.add(numberTotalQuarter);
				}
			}
			if(numDaysYear > 0) {
				dateListYear.add(dateYear);

				ranListYear.add(numberRanYear);
				playedListYear.add(numberPlayedYear);
				totalListYear.add(numberTotalYear);

				final BigDecimal numDaysYearBD = BigDecimal.valueOf(numDaysYear).setScale(10);
				ranDensityListYear.add(numberRanYear.divide(numDaysYearBD, RoundingMode.HALF_UP));
				playedDensityListYear.add(numberPlayedYear.divide(numDaysYearBD, RoundingMode.HALF_UP));
				totalDensityListYear.add(numberTotalYear.divide(numDaysYearBD, RoundingMode.HALF_UP));
			}
		}

		final List<LocalDate> dateListWeek = new ArrayList<>();
		final List<BigDecimal> ranListWeek = new ArrayList<>();
		final List<BigDecimal> playedListWeek = new ArrayList<>();
		final List<BigDecimal> totalListWeek = new ArrayList<>();
		final List<BigDecimal> ranDensityListWeek = new ArrayList<>();
		final List<BigDecimal> playedDensityListWeek = new ArrayList<>();
		final List<BigDecimal> totalDensityListWeek = new ArrayList<>();

		int weekCounter = 0;
		BigDecimal numberRanWeek = BigDecimal.ZERO.setScale(10);
		BigDecimal numberPlayedWeek = BigDecimal.ZERO.setScale(10);
		BigDecimal numberTotalWeek = BigDecimal.ZERO.setScale(10);
		final BigDecimal seven = BigDecimal.valueOf(7).setScale(10);

		for(int d = dateListDay.size() - 1 ; d >= 0 ; d--) {
			if(weekCounter == 7) {
				dateListWeek.add(0, dateListDay.get(d));

				ranListWeek.add(0, numberRanWeek);
				playedListWeek.add(0, numberPlayedWeek);
				totalListWeek.add(0, numberTotalWeek);

				ranDensityListWeek.add(0, numberRanWeek.divide(seven, RoundingMode.HALF_UP));
				playedDensityListWeek.add(0, numberPlayedWeek.divide(seven, RoundingMode.HALF_UP));
				totalDensityListWeek.add(0, numberTotalWeek.divide(seven, RoundingMode.HALF_UP));

				numberRanWeek = BigDecimal.ZERO.setScale(10);
				numberPlayedWeek = BigDecimal.ZERO.setScale(10);
				numberTotalWeek = BigDecimal.ZERO.setScale(10);
				weekCounter = 0;
			}
			numberRanWeek = numberRanWeek.add(ranListDay.get(d));
			numberPlayedWeek = numberPlayedWeek.add(playedListDay.get(d));
			numberTotalWeek = numberTotalWeek.add(totalListDay.get(d));
			weekCounter++;
		}
		dateListWeek.add(0, dateListWeek.get(0).minusDays(7));

		ranListWeek.add(0, numberRanWeek);
		playedListWeek.add(0, numberPlayedWeek);
		totalListWeek.add(0, numberTotalWeek);

		ranDensityListWeek.add(0, numberRanWeek.divide(seven, RoundingMode.HALF_UP));
		playedDensityListWeek.add(0, numberPlayedWeek.divide(seven, RoundingMode.HALF_UP));
		totalDensityListWeek.add(0, numberTotalWeek.divide(seven, RoundingMode.HALF_UP));

		final LocalDate defaultLastDate = dateListDay.get(dateListDay.size() - 1);
		final List<AggOutputDto<?>> charts = new ArrayList<>();
		charts.add(cumulativeChart);
		charts.addAll(runHaar("timeline-haar-day", d -> d.minusDays(1), defaultLastDate, dateListDay, ranListDay, playedListDay, totalListDay));
		charts.addAll(runHaar("timeline-haar-week", d -> d.minusDays(7), defaultLastDate, dateListWeek, ranListWeek, playedListWeek, totalListWeek));
		charts.addAll(
				runHaar("timeline-haar-month", d -> d.minusMonths(1), defaultLastDate, dateListMonth, ranListMonth, playedListMonth, totalListMonth));
		charts.addAll(runHaar("timeline-haar-quarter", d -> d.minusMonths(3), defaultLastDate, dateListQuarter, ranListQuarter, playedListQuarter,
				totalListQuarter));
		charts.addAll(runHaar("timeline-haar-year", d -> d.minusYears(1), defaultLastDate, dateListYear, ranListYear, playedListYear, totalListYear));

		charts.addAll(runHaar("timeline-density-haar-week", d -> d.minusDays(7), defaultLastDate, dateListWeek, ranDensityListWeek,
				playedDensityListWeek, totalDensityListWeek));
		charts.addAll(runHaar("timeline-density-haar-month", d -> d.minusMonths(1), defaultLastDate, dateListMonth, ranDensityListMonth,
				playedDensityListMonth, totalDensityListMonth));
		charts.addAll(runHaar("timeline-density-haar-quarter", d -> d.minusMonths(3), defaultLastDate, dateListQuarter, ranDensityListQuarter,
				playedDensityListQuarter, totalDensityListQuarter));
		charts.addAll(runHaar("timeline-density-haar-year", d -> d.minusYears(1), defaultLastDate, dateListYear, ranDensityListYear,
				playedDensityListYear, totalDensityListYear));

		charts.addAll(runPartialHaar("timeline-partial-haar-day", defaultLastDate, dateListDay, ranListDay, playedListDay, totalListDay));
		charts.addAll(runPartialHaar("timeline-partial-haar-week", defaultLastDate, dateListWeek, ranListWeek, playedListWeek, totalListWeek));
		charts.addAll(runPartialHaar("timeline-partial-haar-month", defaultLastDate, dateListMonth, ranListMonth, playedListMonth, totalListMonth));
		charts.addAll(runPartialHaar("timeline-partial-haar-quarter", defaultLastDate, dateListQuarter, ranListQuarter, playedListQuarter,
				totalListQuarter));
		charts.addAll(runPartialHaar("timeline-partial-haar-year", defaultLastDate, dateListYear, ranListYear, playedListYear, totalListYear));

		charts.addAll(runPartialHaar("timeline-density-partial-haar-week", defaultLastDate, dateListWeek, ranDensityListWeek, playedDensityListWeek,
				totalDensityListWeek));
		charts.addAll(runPartialHaar("timeline-density-partial-haar-month", defaultLastDate, dateListMonth, ranDensityListMonth,
				playedDensityListMonth, totalDensityListMonth));
		charts.addAll(runPartialHaar("timeline-density-partial-haar-quarter", defaultLastDate, dateListQuarter, ranDensityListQuarter,
				playedDensityListQuarter, totalDensityListQuarter));
		charts.addAll(runPartialHaar("timeline-density-partial-haar-year", defaultLastDate, dateListYear, ranDensityListYear, playedDensityListYear,
				totalDensityListYear));

		return charts;
	}

	private List<AggOutputDto<TimelineAggPojo>> runHaar(final String folder, final Function<LocalDate, LocalDate> dateDecrement,
			final LocalDate defaultLastDate, final List<LocalDate> XLin, final List<BigDecimal> RanLin, final List<BigDecimal> PlayedLin,
			final List<BigDecimal> TotalLin) {
		final List<LocalDate> XL = new ArrayList<>(XLin);
		final List<BigDecimal> RanL = new ArrayList<>(RanLin);
		final List<BigDecimal> PlayedL = new ArrayList<>(PlayedLin);
		final List<BigDecimal> TotalL = new ArrayList<>(TotalLin);

		//remove trailing zeros and reset last date
		int i = XL.size() - 1;
		LocalDate lastDate = defaultLastDate;
		while(BigDecimal.ZERO.equals(TotalL.get(i))) {
			lastDate = XL.get(i).minusDays(1);
			XL.remove(i);
			RanL.remove(i);
			PlayedL.remove(i);
			TotalL.remove(i);

			i--;
		}

		//prepend zeros to make size a power of 2
		final int size = POW_2_SIZE_CONVERTER.apply(XL.size());
		final int numToAdd = size - XL.size();
		LocalDate current = XL.get(0);
		for(int s = 0 ; s < numToAdd ; s++) {
			current = dateDecrement.apply(current);
			XL.add(0, current);
			RanL.add(0, BigDecimal.ZERO);
			PlayedL.add(0, BigDecimal.ZERO);
			TotalL.add(0, BigDecimal.ZERO);
		}

		final HaarDecomp<LocalDate, BigDecimal> ranHaar = new HaarDecomp<>(XL, RanL, ADD_FUNC, NEGATIVE_FUNC, DIV_BY_2_FUNC)
				.process(this::processFunc);
		final HaarDecomp<LocalDate, BigDecimal> playedHaar = new HaarDecomp<>(XL, PlayedL, ADD_FUNC, NEGATIVE_FUNC, DIV_BY_2_FUNC)
				.process(this::processFunc);
		final HaarDecomp<LocalDate, BigDecimal> totalHaar = new HaarDecomp<>(XL, TotalL, ADD_FUNC, NEGATIVE_FUNC, DIV_BY_2_FUNC)
				.process(this::processFunc);

		final int L = ranHaar.getL();
		final List<AggOutputDto<TimelineAggPojo>> densityCharts = new ArrayList<>();
		densityCharts.add(createDensityChartForLevel(folder, "V" + L + " (1)", lastDate, ranHaar.plotVL(), playedHaar.plotVL(), totalHaar.plotVL()));
		//TODO currently don't do any filtering, so don't care about V', W, or W'
		//		densityCharts.add(createDensityChartForLevel(folder, "V'" + L, lastDate, ranHaar.plotVpL(), playedHaar.plotVpL(), totalHaar.plotVpL()));
		for(int l = 0 ; l < L ; l++) {
			final int nl = (int) Math.pow(2.0, L - l);
			densityCharts.add(createDensityChartForLevel(folder, "V" + l + " (" + nl + ")", lastDate, ranHaar.plotVl(l), playedHaar.plotVl(l),
					totalHaar.plotVl(l)));
			//			densityCharts
			//					.add(createDensityChartForLevel(folder, "V'" + l, lastDate, ranHaar.plotVpl(l), playedHaar.plotVpl(l), totalHaar.plotVpl(l)));
			//			densityCharts.add(createDensityChartForLevel(folder, "W" + l, lastDate, ranHaar.plotWl(l), playedHaar.plotWl(l), totalHaar.plotWl(l)));
			//			densityCharts
			//					.add(createDensityChartForLevel(folder, "W'" + l, lastDate, ranHaar.plotWpl(l), playedHaar.plotWpl(l), totalHaar.plotWpl(l)));
		}

		return densityCharts;
	}

	private List<AggOutputDto<TimelineAggPojo>> runPartialHaar(final String folder, final LocalDate defaultLastDate, final List<LocalDate> XLin,
			final List<BigDecimal> RanLin, final List<BigDecimal> PlayedLin, final List<BigDecimal> TotalLin) {
		final List<LocalDate> XL = new ArrayList<>(XLin);
		final List<BigDecimal> RanL = new ArrayList<>(RanLin);
		final List<BigDecimal> PlayedL = new ArrayList<>(PlayedLin);
		final List<BigDecimal> TotalL = new ArrayList<>(TotalLin);

		//remove leading zeros
		while(BigDecimal.ZERO.equals(TotalL.get(0))) {
			XL.remove(0);
			RanL.remove(0);
			PlayedL.remove(0);
			TotalL.remove(0);
		}

		//remove trailing zeros and reset last date
		int i = XL.size() - 1;
		LocalDate lastDate = defaultLastDate;
		while(BigDecimal.ZERO.equals(TotalL.get(i))) {
			lastDate = XL.get(i).minusDays(1);
			XL.remove(i);
			RanL.remove(i);
			PlayedL.remove(i);
			TotalL.remove(i);

			i--;
		}

		final RightPartialHaarDecomp<LocalDate, BigDecimal> ranHaar = new RightPartialHaarDecomp<>(XL, RanL, ADD_FUNC, NEGATIVE_FUNC, DIV_BY_2_FUNC)
				.process(this::processFunc);
		final RightPartialHaarDecomp<LocalDate, BigDecimal> playedHaar = new RightPartialHaarDecomp<>(XL, PlayedL, ADD_FUNC, NEGATIVE_FUNC,
				DIV_BY_2_FUNC).process(this::processFunc);
		final RightPartialHaarDecomp<LocalDate, BigDecimal> totalHaar = new RightPartialHaarDecomp<>(XL, TotalL, ADD_FUNC, NEGATIVE_FUNC,
				DIV_BY_2_FUNC).process(this::processFunc);

		final int L = ranHaar.getMaxL();
		final List<AggOutputDto<TimelineAggPojo>> densityCharts = new ArrayList<>();
		densityCharts.add(createDensityChartForLevel(folder, "V" + L + " (1)", lastDate, ranHaar.plotVL(), playedHaar.plotVL(), totalHaar.plotVL()));
		//TODO currently don't do any filtering, so don't care about V', W, or W'
		//		densityCharts.add(createDensityChartForLevel(folder, "V'" + L, lastDate, ranHaar.plotVpL(), playedHaar.plotVpL(), totalHaar.plotVpL()));
		for(int l = 0 ; l < L ; l++) {
			final int nl = (int) Math.pow(2.0, L - l);
			densityCharts.add(createDensityChartForLevel(folder, "V" + l + " (" + nl + ")", lastDate, ranHaar.plotVl(l), playedHaar.plotVl(l),
					totalHaar.plotVl(l)));
			//			densityCharts
			//					.add(createDensityChartForLevel(folder, "V'" + l, lastDate, ranHaar.plotVpl(l), playedHaar.plotVpl(l), totalHaar.plotVpl(l)));
			//			densityCharts.add(createDensityChartForLevel(folder, "W" + l, lastDate, ranHaar.plotWl(l), playedHaar.plotWl(l), totalHaar.plotWl(l)));
			//			densityCharts
			//					.add(createDensityChartForLevel(folder, "W'" + l, lastDate, ranHaar.plotWpl(l), playedHaar.plotWpl(l), totalHaar.plotWpl(l)));
		}

		return densityCharts;
	}

	private AggOutputDto<TimelineAggPojo> createDensityChartForLevel(final String folder, final String title, final LocalDate lastDate,
			final List<Pair<LocalDate, BigDecimal>> Ranl, final List<Pair<LocalDate, BigDecimal>> Playedl,
			final List<Pair<LocalDate, BigDecimal>> Totall) {
		final AggOutputDto<TimelineAggPojo> densityChart = new AggOutputDto<>("Timeline Density " + title, "annotationchart", "AnnotationChart",
				TimelineAggPojo.class, "date", "densityRan", "densityPlayed", "densityTotal");
		densityChart.withNestedFile(folder).getOptions().withHeight(800);

		for(int j = 0 ; j < Ranl.size() ; j++) {
			final LocalDate date = Ranl.get(j).getLeft();
			final BigDecimal ran = Ranl.get(j).getRight();
			final BigDecimal played = Playedl.get(j).getRight();
			final BigDecimal total = Totall.get(j).getRight();

			final TimelineAggPojo pojo = new TimelineAggPojo();
			pojo.setDate(date);
			pojo.setDensityRan(ran);
			pojo.setDensityPlayed(played);
			pojo.setDensityTotal(total);
			densityChart.addRow(pojo);

			final LocalDate dateEnd = j == Ranl.size() - 1 ? lastDate : Ranl.get(j + 1).getLeft().minusDays(1);
			final TimelineAggPojo pojoEnd = new TimelineAggPojo();
			pojoEnd.setDate(dateEnd);
			pojoEnd.setDensityRan(ran);
			pojoEnd.setDensityPlayed(played);
			pojoEnd.setDensityTotal(total);
			densityChart.addRow(pojoEnd);
		}
		return densityChart;
	}

	private List<List<BigDecimal>> processFunc(final List<List<BigDecimal>> W) {
		//TODO
		return W;
	}

}
