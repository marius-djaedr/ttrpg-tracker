package com.me.ttrpg.tracker.aggregators;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.me.io.google.GoogleSheetsIo;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.entities.TtrpgSession;
import com.me.ttrpg.tracker.utils.AggregationUtils;

@Component
public class RefSheetAggregator implements Aggregator {
	private static final Logger logger = LoggerFactory.getLogger(RefSheetAggregator.class);

	private static final String SPREADSHEET_ID = "11lEezEg9rNhk_e7ZCfvW3rZ8O9-L_7gKrKAQy4q5Hqo";
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static final int ROW_LENGTH = 10;

	@Autowired
	private GoogleSheetsIo googleSheetsUtil;

	private List<List<String>> toExport;

	@Override
	public void initialize() {
		toExport = new ArrayList<>();
	}

	@Override
	public void processCampaign(final TtrpgCampaign element) {
		element.getCharactersIPlayed().forEach(ch -> addRow(element, ch.getName(), ch.getSessionsIPlayed()));

		addRow(element, "N/A", element.getSessionsIRan());
		addRow(element, "CHARACTERLESS", element.getSessionsIPlayedNoCharacter());
	}

	private void addRow(final TtrpgCampaign campaign, final String character, final Collection<TtrpgSession> sessions) {
		if(sessions.isEmpty()) {
			return;
		}
		final LocalDate first = sessions.stream().map(TtrpgSession::getPlayDate).min(LocalDate::compareTo).get();
		final LocalDate last = sessions.stream().map(TtrpgSession::getPlayDate).max(LocalDate::compareTo).get();

		final List<String> row = new ArrayList<>();

		row.add(campaign.getName());
		row.add(campaign.getGm());
		row.add(campaign.getSystem());
		row.add(character);
		row.add(first.format(FORMATTER));
		row.add(last.format(FORMATTER));

		final BigDecimal numSessions = AggregationUtils.numberSessions(sessions.stream());
		row.add(String.valueOf(numSessions));

		toExport.add(row);
	}

	@Override
	public void write(final File outputDir) {

		for(int i = 0 ; i < ROW_LENGTH ; i++) {
			toExport.add(new ArrayList<>());
		}

		for(final List<String> row : toExport) {
			for(int i = row.size() ; i < ROW_LENGTH ; i++) {
				row.add("");
			}
		}

		try {
			googleSheetsUtil.writeToSheet(toExport, SPREADSHEET_ID, "A2");
		} catch(final IOException e) {
			logger.error("Unable to write to ref sheet", e);
		}
	}

}
