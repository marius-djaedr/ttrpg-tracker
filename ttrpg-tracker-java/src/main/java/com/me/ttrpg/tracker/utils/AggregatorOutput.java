package com.me.ttrpg.tracker.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.gson.Gson;
import com.me.ttrpg.tracker.annotations.AggColumn;
import com.me.ttrpg.tracker.dtos.aggregation.AggOutputDto;
import com.me.ttrpg.tracker.dtos.aggregation.options.AggChartOptions;

public class AggregatorOutput {
	//https://developers.google.com/chart/interactive/docs/quick_start

	public static <T> void writeToFile(final File file, final AggOutputDto<T> outputDto) throws IOException {
		final List<String> lines = getLines(outputDto);
		Files.write(file.toPath(), lines);
	}

	private static <T> List<String> getLines(final AggOutputDto<T> outputDto) {
		final List<String> lines = new ArrayList<>();
		lines.addAll(TOP_LINES);

		lines.add("google.charts.load('current', {'packages':['" + outputDto.getChartPackage() + "']});");

		lines.addAll(PRE_COLUMN_LINES);
		final List<Field> columnFields = extractColumnFields(outputDto);
		lines.addAll(extractColumnText(columnFields));
		lines.add(extractRows(outputDto, columnFields));
		lines.addAll(extractChartOptions(outputDto));

		lines.addAll(BOTTOM_LINES);
		return lines;
	}

	private static <T> List<Field> extractColumnFields(final AggOutputDto<T> outputDto) {
		final List<Field> columns = FieldUtils.getFieldsListWithAnnotation(outputDto.getPojoClass(), AggColumn.class);
		final List<String> toInclude = outputDto.getColumnsToInclude();
		columns.removeIf(f -> !toInclude.contains(f.getName()));
		columns.forEach(f -> f.setAccessible(true));
		return columns;
	}

	private static synchronized List<String> extractColumnText(final List<Field> columns) {
		final List<String> lines = columns.stream().map(f -> f.getAnnotation(AggColumn.class)).map(AggregatorOutput::extractColumnAnnotation)
				.collect(Collectors.toList());
		return lines;
	}

	private static String extractColumnAnnotation(final AggColumn annotation) {
		if(annotation.role().isEmpty()) {
			return "data.addColumn('" + annotation.type().getDataType() + "', '" + annotation.name() + "');";
		} else {
			return "data.addColumn({type:'" + annotation.type().getDataType() + "',id:'" + annotation.name() + "',role:'" + annotation.role()
					+ "'});";
		}
	}

	private static <T> String extractRows(final AggOutputDto<T> outputDto, final List<Field> columns) {
		return outputDto.getRows().stream().map(r -> extractRow(r, columns)).collect(Collectors.joining(",\n", "data.addRows([\n", "\n]);"));
	}

	private static <T> String extractRow(final T row, final List<Field> columns) {
		return columns.stream().map(f -> extractValue(row, f)).collect(Collectors.joining(",", "[", "]"));
	}

	private static <T> String extractValue(final T row, final Field field) {
		try {
			return field.getAnnotation(AggColumn.class).type().massageValue(field.get(row));
		} catch(IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalArgumentException("Bad Data Row", e);
		}
	}

	private static <T> List<String> extractChartOptions(final AggOutputDto<T> outputDto) {
		final List<String> lines = new ArrayList<>();
		lines.add("");
		lines.add("// Set chart options");
		lines.add("var options = " + jsonify(outputDto.getOptions()));
		lines.add("");
		lines.add("// Instantiate and draw our chart, passing in some options.");
		lines.add("var chart = new google.visualization." + outputDto.getChartType() + "(document.getElementById('chart_div'));");
		return lines;
	}

	private static String jsonify(final AggChartOptions options) {
		return new Gson().toJson(options);
	}

	private static final List<String> TOP_LINES = Arrays.asList(
	//@formatter:off
"<html>",
"<head>",
"<!--https://developers.google.com/chart/interactive/docs/quick_start-->",
"<!--Load the AJAX API-->",
"<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>",
"<script type=\"text/javascript\">",
"// Load the Visualization API and the corechart package."
//@formatter:on
	);

	private static final List<String> PRE_COLUMN_LINES = Arrays.asList(
	//@formatter:off
"google.charts.setOnLoadCallback(drawChart);",
"",
"function drawChart() {",
"// Create the data table.",
"var data = new google.visualization.DataTable();"
//@formatter:on
	);
	private static final List<String> BOTTOM_LINES = Arrays.asList(
//@formatter:off
"chart.draw(data, options);",
"}",
"</script>",
"</head>",
"",
"<body>",
"<!--Div that will hold the pie chart-->",
"<div id=\"chart_div\"></div>",
"</body>",
"</html>"
//@formatter:on
	);
}
