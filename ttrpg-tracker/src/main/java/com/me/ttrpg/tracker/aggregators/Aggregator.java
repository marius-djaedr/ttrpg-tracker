package com.me.ttrpg.tracker.aggregators;

import java.io.File;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;

public interface Aggregator {
	void initialize();

	void processCampaign(TtrpgCampaign element);

	void write(File outputDir);
}
