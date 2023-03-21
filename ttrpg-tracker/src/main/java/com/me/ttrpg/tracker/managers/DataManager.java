package com.me.ttrpg.tracker.managers;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.me.io.JsonIo;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.entities.TtrpgDataContainer;
import com.me.ttrpg.tracker.dtos.view.ViewCampaign;
import com.me.util.utils.FileUtils;

@Component
public class DataManager {//TODO implements ActionStateProvider<Map<String, TtrpgCampaign>> {
	private static final Logger logger = LoggerFactory.getLogger(DataManager.class);

	private static final File DATA_FOLDER = new File("src/main/resources/data");

	@Autowired
	private JsonIo jsonIo;

	private Map<String, TtrpgCampaign> campaignMap;

	//	@Override
	//	public Map<String, TtrpgCampaign> getState() {
	//		return campaignMap;
	//	}
	//
	//	@Override
	//	public void setState(final Map<String, TtrpgCampaign> state) {
	//		this.campaignMap = state;
	//	}

	@PostConstruct
	public void initialize() {
		final File latest = FileUtils.getLatestFile(DATA_FOLDER);
		logger.info("Reading from " + latest.getAbsolutePath());

		final TtrpgDataContainer container = jsonIo.readOneFromFile(TtrpgDataContainer.class, latest);
		campaignMap = container.getCampaignMap();
	}

	@PreDestroy
	public void save() {
		final File generated = new File(DATA_FOLDER,
				"campaigns-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".json");
		logger.info("Writing to " + generated.getAbsolutePath());

		final TtrpgDataContainer container = new TtrpgDataContainer();
		container.setCampaignMap(campaignMap);
		jsonIo.writeOneToFile(container, generated);
	}

	public Collection<TtrpgCampaign> getCampaigns() {
		return campaignMap.values();
	}

	public TtrpgCampaign getCampaign(final String id) {
		return campaignMap.get(id);
	}

	public void addCampaign(final TtrpgCampaign campaign) {
		campaignMap.put(campaign.getId(), campaign);
	}

	public void removeCampaign(final String id) {
		campaignMap.remove(id);
	}

	public List<ViewCampaign> getCampaignViews() {
		return campaignMap.values().stream().map(ViewCampaign::new).collect(Collectors.toList());
	}
}
