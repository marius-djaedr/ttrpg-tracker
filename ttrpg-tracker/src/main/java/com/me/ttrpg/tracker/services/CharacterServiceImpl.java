package com.me.ttrpg.tracker.services;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.me.gui.swing.views.crud.CrudService;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;
import com.me.ttrpg.tracker.dtos.view.ViewCampaign;
import com.me.ttrpg.tracker.dtos.view.ViewCharacter;
import com.me.ttrpg.tracker.managers.DataManager;

@Service
public class CharacterServiceImpl implements CrudService<ViewCharacter> {

	@Autowired
	private DataManager dataManager;

	@Override
	public List<ViewCharacter> getAll() {
		return dataManager.getCampaignViews().stream().map(ViewCampaign::getCharactersIPlayed).flatMap(List::stream).collect(Collectors.toList());
	}

	@Override
	public void create(final ViewCharacter entity) {
		final TtrpgCharacter newEntity = new TtrpgCharacter();
		newEntity.setId(entity.getId());

		commonUpdate(newEntity, entity);

		commonAction(entity, ca -> ca.addCharacterIPlayed(newEntity));
	}

	@Override
	public void update(final ViewCharacter entity) {
		commonAction(entity, ca -> commonUpdate(ca.getCharacterIPlayed(entity.getId()), entity));
	}

	private void commonUpdate(final TtrpgCharacter existing, final ViewCharacter updated) {
		existing.setName(updated.getName());
		existing.setRace(updated.getRace());
		existing.setGender(updated.getGender());
		existing.setClassRole(updated.getClassRole());
		existing.setDiedInGameFlg(updated.getDiedInGameFlg());
		existing.setTragicStoryFlg(updated.getTragicStoryFlg());
	}

	@Override
	public void delete(final ViewCharacter entity) {
		commonAction(entity, ca -> ca.removeCharacterIPlayed(entity.getId()));
	}

	private void commonAction(final ViewCharacter character, final Consumer<TtrpgCampaign> action) {
		final String campaignId = character.getParentCampaign().getId();
		final TtrpgCampaign campaign = dataManager.getCampaign(campaignId);
		if(campaign == null) {
			throw new IllegalArgumentException("Invalid campaign ID " + campaignId);
		}

		action.accept(campaign);
	}
}
