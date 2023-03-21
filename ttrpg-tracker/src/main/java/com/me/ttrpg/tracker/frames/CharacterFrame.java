package com.me.ttrpg.tracker.frames;

import java.awt.GridBagConstraints;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.me.gui.swing.utils.GuiUtils;
import com.me.gui.swing.utils.GuiUtils.UpdatePopupField;
import com.me.gui.swing.views.DropdownJComponent;
import com.me.gui.swing.views.TrinaryJComponent;
import com.me.gui.swing.views.crud.AbstractCrudFrame;
import com.me.gui.swing.views.crud.AbstractCrudTableModel;
import com.me.gui.swing.views.crud.CrudService;
import com.me.gui.swing.views.crud.GenericRowFilter;
import com.me.ttrpg.tracker.dtos.view.ViewCampaign;
import com.me.ttrpg.tracker.dtos.view.ViewCharacter;
import com.me.ttrpg.tracker.frames.CampaignFrame.CampaignSelectedListener;
import com.me.ttrpg.tracker.frames.models.CharacterTableModel;

@Component
public class CharacterFrame extends AbstractCrudFrame<ViewCharacter> implements CampaignSelectedListener {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private CrudService<ViewCharacter> characterService;

	private JTextField selectedCampaignField;
	private ViewCampaign selectedCampaignEntity;

	@Override
	public String tabName() {
		return "Characters";
	}

	@Override
	public double order() {
		return 2.0;
	}

	@Override
	protected CrudService<ViewCharacter> getService() {
		return characterService;
	}

	@Override
	public void updateSelectedCampaign(final ViewCampaign selected) {
		selectedCampaignEntity = selected;
		if(selected == null) {
			selectedCampaignField.setText("--no campaign selected--");
			updateFilter(null);
		} else {
			selectedCampaignField.setText(selected.getName());
			updateFilter(new GenericRowFilter<AbstractCrudTableModel<ViewCharacter>>(CharacterTableModel.campaignIdIndex(),
					o -> selected.getId().equals(o)));
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

		panel.add(selectedCampaignField, c);
		c.gridx++;
		panel.add(deselectCampaignButton, c);

		return panel;
	}

	@Override
	protected AbstractCrudTableModel<ViewCharacter> newTableModel() {
		return new CharacterTableModel();
	}

	@Override
	protected ViewCharacter newEntity(final String id) {
		if(selectedCampaignEntity == null) {
			throw new IllegalArgumentException("Must select campaign");
		}
		return new ViewCharacter(id, selectedCampaignEntity);
	}

	@Override
	protected void selectEntity(final ViewCharacter entity) {
		context.getBeansOfType(CharacterSelectedListener.class).values().forEach(l -> l.updateSelectedCharacter(entity));
	}

	public static interface CharacterSelectedListener {
		void updateSelectedCharacter(ViewCharacter selected);
	}

	@Override
	protected List<UpdatePopupField<ViewCharacter>> getUpdateFields() {
		return Arrays.asList(CharacterUpdatePopupField.values());
	}

	public enum CharacterUpdatePopupField implements UpdatePopupField<ViewCharacter> {
		NAME("Name", JTextField::new, JTextField::getText, JTextField::setText, ViewCharacter::getName, ViewCharacter::setName),
		RACE("Race", JTextField::new, JTextField::getText, JTextField::setText, ViewCharacter::getRace, ViewCharacter::setRace),
		CLASS("Class/Role", JTextField::new, JTextField::getText, JTextField::setText, ViewCharacter::getClassRole, ViewCharacter::setClassRole),
		GENDER("Gender", () -> new DropdownJComponent(new String[]{"male", "female", "none (lean male)", "none (lean female)"}),
				DropdownJComponent::getValue, DropdownJComponent::setValue, ViewCharacter::getGender, ViewCharacter::setGender),
		TRAGIC("Tragic Backstory?", TrinaryJComponent::new, TrinaryJComponent::getValue, TrinaryJComponent::setValue,
				ViewCharacter::getTragicStoryFlg, ViewCharacter::setTragicStoryFlg),
		DIED("Died in game?", TrinaryJComponent::new, TrinaryJComponent::getValue, TrinaryJComponent::setValue, ViewCharacter::getDiedInGameFlg,
				ViewCharacter::setDiedInGameFlg);

		private <T extends JComponent, O> CharacterUpdatePopupField(final String label, final Supplier<T> fieldConstructor,
				final Function<T, O> fieldGetter, final BiConsumer<T, O> fieldSetter, final Function<ViewCharacter, O> entityGetter,
				final BiConsumer<ViewCharacter, O> entitySetter) {

			this.label = label;
			this.fieldConstructor = (Supplier<JComponent>) fieldConstructor;
			this.fieldGetter = (Function<JComponent, Object>) fieldGetter;
			this.fieldSetter = (BiConsumer<JComponent, Object>) fieldSetter;
			this.entityGetter = (Function<ViewCharacter, Object>) entityGetter;
			this.entitySetter = (BiConsumer<ViewCharacter, Object>) entitySetter;
		}

		private final String label;
		private final Supplier<JComponent> fieldConstructor;
		private final Function<JComponent, Object> fieldGetter;
		private final BiConsumer<JComponent, Object> fieldSetter;
		private final Function<ViewCharacter, Object> entityGetter;
		private final BiConsumer<ViewCharacter, Object> entitySetter;

		@Override
		public JComponent instantiateField(final ViewCharacter existing) {
			final JComponent field = fieldConstructor.get();
			fieldSetter.accept(field, entityGetter.apply(existing));
			return field;
		}

		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public void pullFromField(final JComponent field, final ViewCharacter toFill) {
			entitySetter.accept(toFill, fieldGetter.apply(field));
		}
	}
}
