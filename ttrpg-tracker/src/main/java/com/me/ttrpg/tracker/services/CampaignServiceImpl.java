package com.me.ttrpg.tracker.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.me.gui.swing.views.crud.CrudService;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.view.ViewCampaign;
import com.me.ttrpg.tracker.managers.DataManager;

@Service
public class CampaignServiceImpl implements CrudService<ViewCampaign> {

	@Autowired
	private DataManager dataManager;

	@Override
	public List<ViewCampaign> getAll() {
		return dataManager.getCampaignViews();
	}

	@Override
	public void create(final ViewCampaign entity) {
		final TtrpgCampaign newEntity = new TtrpgCampaign();
		newEntity.setId(entity.getId());

		commonUpdate(newEntity, entity);

		dataManager.addCampaign(newEntity);
	}

	@Override
	public void update(final ViewCampaign entity) {
		final TtrpgCampaign campaign = dataManager.getCampaign(entity.getId());
		commonUpdate(campaign, entity);
	}

	private void commonUpdate(final TtrpgCampaign existing, final ViewCampaign updated) {
		existing.setName(updated.getName());
		existing.setGm(updated.getGm());
		existing.setSystem(updated.getSystem());
		existing.setCompletedFlg(updated.getCompletedFlg());
	}

	@Override
	public void delete(final ViewCampaign entity) {
		dataManager.removeCampaign(entity.getId());
	}

}
