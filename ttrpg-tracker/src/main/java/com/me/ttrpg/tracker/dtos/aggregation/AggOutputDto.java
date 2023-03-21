package com.me.ttrpg.tracker.dtos.aggregation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.me.ttrpg.tracker.dtos.aggregation.options.AggChartOptions;

public class AggOutputDto<T> {
	private final AggChartOptions options;
	private final String chartPackage;
	private final String chartType;
	private final Class<T> pojoClass;
	private final List<String> columnsToInclude;
	private final List<T> rows = new ArrayList<>();
	private String nestedFile;

	public AggOutputDto(final String title, final String chartPackage, final String chartType, final Class<T> pojoClass,
			final String... columnsToInclude) {
		this.options = new AggChartOptions(title);
		this.chartPackage = chartPackage;
		this.chartType = chartType;
		this.pojoClass = pojoClass;
		this.columnsToInclude = Arrays.asList(columnsToInclude);
	}

	public AggChartOptions getOptions() {
		return options;
	}

	public String getChartPackage() {
		return chartPackage;
	}

	public String getChartType() {
		return chartType;
	}

	public Class<T> getPojoClass() {
		return pojoClass;
	}

	public List<String> getColumnsToInclude() {
		return columnsToInclude;
	}

	public List<T> getRows() {
		return rows;
	}

	public void addRow(final T pojo) {
		if(!pojoClass.isAssignableFrom(pojo.getClass())) {
			throw new IllegalArgumentException("Invalid POJO class! Expected [" + pojoClass + "] but was [" + pojo.getClass() + "]");
		}
		rows.add(pojo);
	}

	public String getNestedFile() {
		return nestedFile;
	}

	public AggOutputDto<T> withNestedFile(final String nestedFile) {
		this.nestedFile = nestedFile;
		return this;
	}
}
