package com.me.ttrpg.tracker.dtos.aggregation;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.me.ttrpg.tracker.annotations.AggColumn;

public class TimelineAggPojo {

	@AggColumn(name = "Date", type = AggColumn.AggColumnType.DATE)
	private LocalDate date;

	@AggColumn(name = "Ran", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal densityRan;

	@AggColumn(name = "Played", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal densityPlayed;

	@AggColumn(name = "Total", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal densityTotal;

	@AggColumn(name = "Ran", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal cumulativeRan;

	@AggColumn(name = "Played", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal cumulativePlayed;

	@AggColumn(name = "Ratio", type = AggColumn.AggColumnType.NUMBER)
	private BigDecimal ratio;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(final LocalDate date) {
		this.date = date;
	}

	public BigDecimal getDensityRan() {
		return densityRan;
	}

	public void setDensityRan(final BigDecimal densityRan) {
		this.densityRan = densityRan;
	}

	public BigDecimal getDensityPlayed() {
		return densityPlayed;
	}

	public void setDensityPlayed(final BigDecimal densityPlayed) {
		this.densityPlayed = densityPlayed;
	}

	public BigDecimal getDensityTotal() {
		return densityTotal;
	}

	public void setDensityTotal(final BigDecimal densityTotal) {
		this.densityTotal = densityTotal;
	}

	public BigDecimal getCumulativeRan() {
		return cumulativeRan;
	}

	public void setCumulativeRan(final BigDecimal cumulativeRan) {
		this.cumulativeRan = cumulativeRan;
	}

	public BigDecimal getCumulativePlayed() {
		return cumulativePlayed;
	}

	public void setCumulativePlayed(final BigDecimal cumulativePlayed) {
		this.cumulativePlayed = cumulativePlayed;
	}

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(final BigDecimal ratio) {
		this.ratio = ratio;
	}

}
