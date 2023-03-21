package com.me.ttrpg.tracker.frames.util;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JComponent;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.me.gui.swing.utils.GuiUtils;
import com.me.gui.swing.utils.GuiUtils.UpdatePopupField;
import com.me.gui.swing.views.crud.CrudService;
import com.me.ttrpg.tracker.dtos.view.ViewCampaign;
import com.me.ttrpg.tracker.dtos.view.ViewCharacter;
import com.me.ttrpg.tracker.dtos.view.ViewSession;
import com.me.ttrpg.tracker.frames.CampaignFrame;
import com.me.ttrpg.tracker.frames.CharacterFrame;
import com.me.ttrpg.tracker.frames.SessionFrame;

public abstract class AbstractOneShotUtilityButton implements UtilityButton {
	private static final Logger logger = LoggerFactory.getLogger(AbstractOneShotUtilityButton.class);

	@Autowired
	private CrudService<ViewCampaign> campaignService;
	@Autowired
	private CrudService<ViewCharacter> characterService;
	@Autowired
	private CrudService<ViewSession> sessionService;

	@Override
	public void actionListener(final ActionEvent e) {
		final boolean includeCharacter = includeCharacter();
		final ViewSession session = buildOneShot(includeCharacter);
		if(GuiUtils.entityForm(buildCatSpecs(includeCharacter), session)) {
			campaignService.create(session.getParentCampaign());
			if(includeCharacter) {
				characterService.create(session.getParentCharacter());
			}
			sessionService.create(session);
		} else {
			logger.warn("edit cancelled");
		}
	}

	private List<Pair<String, List<UpdatePopupField<ViewSession>>>> buildCatSpecs(final boolean includeCharacter) {
		final List<Pair<String, List<UpdatePopupField<ViewSession>>>> catSpecs = new ArrayList<>();

		final List<UpdatePopupField<ViewSession>> campaignFields = Stream.of(CampaignFrame.CampaignUpdatePopupField.values())
				.map(this::convertCampaignEnum).collect(Collectors.toList());
		catSpecs.add(Pair.of("Campaign", campaignFields));

		if(includeCharacter) {
			final List<UpdatePopupField<ViewSession>> characterFields = Stream.of(CharacterFrame.CharacterUpdatePopupField.values())
					.map(this::convertCharacterEnum).collect(Collectors.toList());
			catSpecs.add(Pair.of("Character", characterFields));
		}

		catSpecs.add(Pair.of("Session", Arrays.asList(SessionFrame.SessionUpdatePopupField.values())));

		return catSpecs;
	}

	private ViewSession buildOneShot(final boolean includeCharacter) {
		final ViewCampaign parentCampaign = new ViewCampaign(UUID.randomUUID().toString());
		parentCampaign.setName("one shot");
		parentCampaign.setCompletedFlg(true);

		ViewCharacter parentCharacter = null;
		if(includeCharacter) {
			parentCharacter = new ViewCharacter(UUID.randomUUID().toString(), parentCampaign);
		} else {
			parentCampaign.setGm("ME");
		}

		final ViewSession session = new ViewSession(UUID.randomUUID().toString(), parentCharacter, parentCampaign);
		return session;
	}

	private UpdatePopupField<ViewSession> convertCampaignEnum(final UpdatePopupField<ViewCampaign> campaignField) {
		return new UpdatePopupField<ViewSession>() {

			@Override
			public void pullFromField(final JComponent field, final ViewSession toFill) {
				campaignField.pullFromField(field, toFill.getParentCampaign());
			}

			@Override
			public JComponent instantiateField(final ViewSession existing) {
				return campaignField.instantiateField(existing.getParentCampaign());
			}

			@Override
			public String getLabel() {
				return "Campaign " + campaignField.getLabel();
			}
		};
	}

	private UpdatePopupField<ViewSession> convertCharacterEnum(final UpdatePopupField<ViewCharacter> characterField) {
		return new UpdatePopupField<ViewSession>() {

			@Override
			public void pullFromField(final JComponent field, final ViewSession toFill) {
				characterField.pullFromField(field, toFill.getParentCharacter());
			}

			@Override
			public JComponent instantiateField(final ViewSession existing) {
				return characterField.instantiateField(existing.getParentCharacter());
			}

			@Override
			public String getLabel() {
				return "Character " + characterField.getLabel();
			}
		};
	}

	protected abstract boolean includeCharacter();
}
