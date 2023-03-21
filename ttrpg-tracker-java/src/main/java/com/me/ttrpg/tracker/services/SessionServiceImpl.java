package com.me.ttrpg.tracker.services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.me.gui.swing.views.crud.CrudService;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;
import com.me.ttrpg.tracker.dtos.entities.TtrpgSession;
import com.me.ttrpg.tracker.dtos.view.ViewCampaign;
import com.me.ttrpg.tracker.dtos.view.ViewCharacter;
import com.me.ttrpg.tracker.dtos.view.ViewSession;
import com.me.ttrpg.tracker.managers.DataManager;

@Service
public class SessionServiceImpl implements CrudService<ViewSession> {

	@Autowired
	private DataManager dataManager;

	@Override
	public List<ViewSession> getAll() {
		final List<ViewCampaign> campaigns = dataManager.getCampaignViews();

		final List<ViewSession> data = new ArrayList<>();
		campaigns.stream().map(ViewCampaign::getSessionsIRan).flatMap(List::stream).forEachOrdered(data::add);
		campaigns.stream().map(ViewCampaign::getSessionsIPlayedNoCharacter).flatMap(List::stream).forEachOrdered(data::add);
		campaigns.stream().map(ViewCampaign::getCharactersIPlayed).flatMap(List::stream).map(ViewCharacter::getSessionsIPlayed).flatMap(List::stream)
				.forEachOrdered(data::add);

		return data;
	}

	@Override
	public void create(final ViewSession entity) {
		final TtrpgSession newEntity = new TtrpgSession();
		newEntity.setId(entity.getId());
		newEntity.setPlayDate(entity.getPlayDate());
		newEntity.setShortFlg(entity.getShortFlg());

		commonAction(entity, ca -> ca.addSessionIRan(newEntity), ca -> ca.addSessionIPlayedNoCharacter(newEntity),
				ch -> ch.addSessionIPlayed(newEntity));
	}

	@Override
	public void update(final ViewSession entity) {
		commonAction(entity, ca -> updateRan(ca, entity), ca -> updatePlayedNoCharacter(ca, entity),
				ch -> commonUpdate(ch.getSessionIPlayed(entity.getId()), entity));
	}

	private void updatePlayedNoCharacter(final TtrpgCampaign ca, final ViewSession updated) {
		final String sessionId = updated.getId();
		TtrpgSession existing = ca.getSessionIPlayedNoCharacter(sessionId);
		if(existing == null) {
			//means we are changing from ran to played
			existing = ca.getSessionIRan(sessionId);
			ca.removeSessionIRan(sessionId);
			ca.addSessionIPlayedNoCharacter(existing);
		}
		commonUpdate(existing, updated);
	}

	private void updateRan(final TtrpgCampaign ca, final ViewSession updated) {
		final String sessionId = updated.getId();
		TtrpgSession existing = ca.getSessionIRan(sessionId);
		if(existing == null) {
			//means we are changing from played to ran
			existing = ca.getSessionIPlayedNoCharacter(sessionId);
			ca.removeSessionIPlayedNoCharacter(sessionId);
			ca.addSessionIRan(existing);
		}
		commonUpdate(existing, updated);
	}

	private void commonUpdate(final TtrpgSession existing, final ViewSession updated) {
		existing.setPlayDate(updated.getPlayDate());
		existing.setShortFlg(updated.getShortFlg());
	}

	@Override
	public void delete(final ViewSession entity) {
		commonAction(entity, ca -> ca.removeSessionIRan(entity.getId()), ca -> ca.removeSessionIPlayedNoCharacter(entity.getId()),
				ch -> ch.removeSessionIPlayed(entity.getId()));
	}

	private void commonAction(final ViewSession session, final Consumer<TtrpgCampaign> actionIfRan,
			final Consumer<TtrpgCampaign> actionIfPlayedNoCharacter, final Consumer<TtrpgCharacter> actionIfPlayed) {
		final String campaignId = session.getParentCampaign().getId();
		final TtrpgCampaign campaign = dataManager.getCampaign(campaignId);
		if(campaign == null) {
			throw new IllegalArgumentException("Invalid campaign ID " + campaignId);
		}

		if(session.getParentCharacter() == null) {
			if(session.getPlayed() == null || !session.getPlayed()) {
				actionIfRan.accept(campaign);
			} else {
				actionIfPlayedNoCharacter.accept(campaign);
			}
		} else {
			final String characterId = session.getParentCharacter().getId();
			final TtrpgCharacter character = campaign.getCharacterIPlayed(characterId);
			if(character == null) {
				throw new IllegalArgumentException("Invalid character ID [" + characterId + "] for campaign " + campaign);
			}
			actionIfPlayed.accept(character);
		}
	}

}
