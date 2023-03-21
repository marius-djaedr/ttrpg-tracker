package com.me.ttrpg.tracker.frames;

import java.awt.GridBagConstraints;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.lgooddatepicker.components.DatePicker;
import com.me.gui.swing.utils.GuiUtils;
import com.me.gui.swing.utils.GuiUtils.UpdatePopupField;
import com.me.gui.swing.views.TrinaryJComponent;
import com.me.gui.swing.views.crud.AbstractCrudFrame;
import com.me.gui.swing.views.crud.AbstractCrudTableModel;
import com.me.gui.swing.views.crud.CrudService;
import com.me.gui.swing.views.crud.GenericRowFilter;
import com.me.ttrpg.tracker.dtos.view.ViewCampaign;
import com.me.ttrpg.tracker.dtos.view.ViewCharacter;
import com.me.ttrpg.tracker.dtos.view.ViewSession;
import com.me.ttrpg.tracker.frames.CampaignFrame.CampaignSelectedListener;
import com.me.ttrpg.tracker.frames.CharacterFrame.CharacterSelectedListener;
import com.me.ttrpg.tracker.frames.models.SessionTableModel;

@Component
public class SessionFrame extends AbstractCrudFrame<ViewSession> implements CampaignSelectedListener, CharacterSelectedListener {

	@Autowired
	private CrudService<ViewSession> sessionService;

	private JTextField selectedCampaignField;
	private JTextField selectedCharacterField;
	private ViewCampaign selectedCampaignEntity;
	private ViewCharacter selectedCharacterEntity;

	@Override
	public String tabName() {
		return "Sessions";
	}

	@Override
	public double order() {
		return 3.0;
	}

	@Override
	protected CrudService<ViewSession> getService() {
		return sessionService;
	}

	@Override
	public void updateSelectedCampaign(final ViewCampaign selected) {
		selectedCampaignEntity = selected;
		selectedCharacterEntity = null;

		commonUpdate();
	}

	@Override
	public void updateSelectedCharacter(final ViewCharacter selected) {
		selectedCharacterEntity = selected;
		selectedCampaignEntity = selected == null ? selectedCampaignEntity : selected.getParentCampaign();

		commonUpdate();
	}

	private void commonUpdate() {
		selectedCampaignField.setText(selectedCampaignEntity == null ? "--no campaign selected--" : selectedCampaignEntity.getName());
		selectedCharacterField.setText(selectedCharacterEntity == null ? "--no character selected--" : selectedCharacterEntity.getName());

		final Map<Integer, Predicate<Object>> filterMap = new HashMap<>();
		if(selectedCampaignEntity != null) {
			filterMap.put(SessionTableModel.campaignIdIndex(), o -> selectedCampaignEntity.getId().equals(o));
		}
		if(selectedCharacterEntity != null) {
			filterMap.put(SessionTableModel.characterIdIndex(), o -> selectedCharacterEntity.getId().equals(o));
		}

		if(filterMap.isEmpty()) {
			updateFilter(null);
		} else {
			updateFilter(new GenericRowFilter<AbstractCrudTableModel<ViewSession>>(filterMap, new HashMap<>()));
		}

	}

	@Override
	protected JPanel buildAdditionalTop() {
		final GridBagConstraints c = new GridBagConstraints();
		final JPanel panel = GuiUtils.commonBuildPanel(c);

		selectedCampaignField = new JTextField();
		selectedCampaignField.setEditable(false);
		selectedCampaignField.setText("--no campaign selected--");

		final JButton deselectCampaignButton = new JButton("Deselect Campaign");
		deselectCampaignButton.addActionListener(e -> updateSelectedCampaign(null));

		selectedCharacterField = new JTextField();
		selectedCharacterField.setEditable(false);
		selectedCharacterField.setText("--no character selected--");

		final JButton deselectCharacterButton = new JButton("Deselect Character");
		deselectCharacterButton.addActionListener(e -> updateSelectedCharacter(null));

		panel.add(selectedCampaignField, c);
		c.gridx++;
		panel.add(deselectCampaignButton, c);

		c.gridy++;
		c.gridx = 0;
		panel.add(selectedCharacterField, c);
		c.gridx++;
		panel.add(deselectCharacterButton, c);

		return panel;
	}

	@Override
	protected AbstractCrudTableModel<ViewSession> newTableModel() {
		return new SessionTableModel();
	}

	@Override
	protected ViewSession newEntity(final String id) {
		if(selectedCharacterEntity == null && selectedCampaignEntity == null) {
			throw new IllegalArgumentException("Must select either character (for session played) or campaign (for session ran)");
		} else if((selectedCharacterEntity != null && selectedCampaignEntity != null)
				&& !StringUtils.equals(selectedCharacterEntity.getParentCampaign().getId(), selectedCampaignEntity.getId())) {
			throw new IllegalArgumentException(
					"(shouldn't be possible) Ambiguous selection for character and campaign. Must select either character (for session played) or campaign (for session ran)");
		} else if(selectedCharacterEntity != null && selectedCampaignEntity == null) {
			return new ViewSession(id, selectedCharacterEntity, selectedCharacterEntity.getParentCampaign());
		} else {
			return new ViewSession(id, selectedCharacterEntity, selectedCampaignEntity);
		}
	}

	@Override
	protected List<UpdatePopupField<ViewSession>> getUpdateFields() {
		return Arrays.asList(SessionUpdatePopupField.values());
	}

	public enum SessionUpdatePopupField implements UpdatePopupField<ViewSession> {
		PLAY_DATE("Session Date", DatePicker::new, DatePicker::getDate, DatePicker::setDate, ViewSession::getPlayDate, ViewSession::setPlayDate),
		IS_SHORT("Short Session?", TrinaryJComponent::new, TrinaryJComponent::getValue, TrinaryJComponent::setValue, ViewSession::getShortFlg,
				ViewSession::setShortFlg),
		IS_PLAY("Play No Character?", TrinaryJComponent::new, TrinaryJComponent::getValue, TrinaryJComponent::setValue, ViewSession::getPlayed,
				ViewSession::setPlayed);

		private <T extends JComponent, O> SessionUpdatePopupField(final String label, final Supplier<T> fieldConstructor,
				final Function<T, O> fieldGetter, final BiConsumer<T, O> fieldSetter, final Function<ViewSession, O> entityGetter,
				final BiConsumer<ViewSession, O> entitySetter) {

			this.label = label;
			this.fieldConstructor = (Supplier<JComponent>) fieldConstructor;
			this.fieldGetter = (Function<JComponent, Object>) fieldGetter;
			this.fieldSetter = (BiConsumer<JComponent, Object>) fieldSetter;
			this.entityGetter = (Function<ViewSession, Object>) entityGetter;
			this.entitySetter = (BiConsumer<ViewSession, Object>) entitySetter;
		}

		private final String label;
		private final Supplier<JComponent> fieldConstructor;
		private final Function<JComponent, Object> fieldGetter;
		private final BiConsumer<JComponent, Object> fieldSetter;
		private final Function<ViewSession, Object> entityGetter;
		private final BiConsumer<ViewSession, Object> entitySetter;

		@Override
		public JComponent instantiateField(final ViewSession existing) {
			final JComponent field = fieldConstructor.get();
			fieldSetter.accept(field, entityGetter.apply(existing));
			return field;
		}

		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public void pullFromField(final JComponent field, final ViewSession toFill) {
			entitySetter.accept(toFill, fieldGetter.apply(field));
		}
	}

}
