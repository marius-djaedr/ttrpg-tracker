package com.me.ttrpg.tracker.aggregators;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.me.io.JsonIo;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.entities.TtrpgDataContainer;

@Component
public class BackupAggregator implements Aggregator {

	@Autowired
	private JsonIo jsonIo;

	private Map<String, TtrpgCampaign> campaignMap;

	@Override
	public void initialize() {
		campaignMap = new HashMap<>();
	}

	@Override
	public void processCampaign(final TtrpgCampaign element) {
		campaignMap.put(element.getId(), element);
	}

	@Override
	public void write(final File outputDir) {
		final TtrpgDataContainer container = new TtrpgDataContainer();
		container.setCampaignMap(campaignMap);
		final File file = new File(outputDir, "backup.json");
		jsonIo.writeOneToFile(container, file);
	}

}
