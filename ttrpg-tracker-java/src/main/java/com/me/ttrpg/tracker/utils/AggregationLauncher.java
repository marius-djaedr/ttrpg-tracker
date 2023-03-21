package com.me.ttrpg.tracker.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.me.gui.swing.LaunchWrapper;
import com.me.io.google.GoogleDriveIo;
import com.me.ttrpg.tracker.aggregators.Aggregator;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.managers.DataManager;

@Component
public class AggregationLauncher {
	private static final Logger logger = LoggerFactory.getLogger(AggregationLauncher.class);

	@Autowired
	private ApplicationContext context;
	@Autowired
	private DataManager dataManager;
	@Autowired
	private LaunchWrapper launchWrapper;
	@Autowired
	private GoogleDriveIo driveIo;

	public File launch() {
		logger.info("Starting Aggregations");
		final Collection<Aggregator> aggregators = context.getBeansOfType(Aggregator.class).values();
		if(aggregators.isEmpty()) {
			logger.warn("No valid aggregators found");
			return null;
		}

		logger.info("Starting initialization");
		aggregators.forEach(this::initialize);
		logger.info("Starting process");
		final Collection<TtrpgCampaign> elements = dataManager.getCampaigns();
		elements.forEach(e -> aggregators.forEach(a -> a.processCampaign(e)));

		logger.info("Starting write");
		final String folderName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
		final File outputDir = new File("src/main/resources/aggregation/" + folderName);
		outputDir.mkdirs();
		aggregators.forEach(a -> a.write(outputDir));

		launchWrapper.executeOnNewThread(() -> {
			logger.info("Starting send");
			try {
				driveIo.directoryReplace(outputDir, Arrays.asList("TTRPG", "latest-aggregation"));
			} catch(final IOException e) {
				logger.error("Unable to transfer to drive", e);
			}
		});

		logger.info("Job's Done");
		return outputDir;
	}

	private void initialize(final Aggregator aggregator) {
		logger.info("Initializing {}", aggregator.getClass().getCanonicalName());
		aggregator.initialize();
	}
}
