package com.me.ttrpg.tracker.frames;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.me.gui.swing.utils.GuiUtils.UpdatePopupField;
import com.me.gui.swing.views.TrinaryJComponent;
import com.me.gui.swing.views.crud.AbstractCrudFrame;
import com.me.gui.swing.views.crud.AbstractCrudTableModel;
import com.me.gui.swing.views.crud.CrudService;
import com.me.ttrpg.tracker.dtos.view.ViewCampaign;
import com.me.ttrpg.tracker.frames.models.CampaignTableModel;

@Component
public class CampaignFrame extends AbstractCrudFrame<ViewCampaign> {

	@Autowired
	private CrudService<ViewCampaign> campaignService;
	@Autowired
	private ApplicationContext context;

	@Override
	public String tabName() {
		return "Campaigns";
	}

	@Override
	public double order() {
		return 1.0;
	}

	@Override
	protected CrudService<ViewCampaign> getService() {
		return campaignService;
	}

	@Override
	protected AbstractCrudTableModel<ViewCampaign> newTableModel() {
		return new CampaignTableModel();
	}

	@Override
	protected ViewCampaign newEntity(final String id) {
		return new ViewCampaign(id);
	}

	@Override
	protected void selectEntity(final ViewCampaign entity) {
		context.getBeansOfType(CampaignSelectedListener.class).values().forEach(l -> l.updateSelectedCampaign(entity));
	}

	public static interface CampaignSelectedListener {
		void updateSelectedCampaign(ViewCampaign selected);
	}

	@Override
	protected List<UpdatePopupField<ViewCampaign>> getUpdateFields() {

		return Arrays.asList(CampaignUpdatePopupField.values());
	}

	public static enum CampaignUpdatePopupField implements UpdatePopupField<ViewCampaign> {
		NAME("Name", JTextField::new, JTextField::getText, JTextField::setText, ViewCampaign::getName, ViewCampaign::setName),
		SYSTEM("System", JTextField::new, JTextField::getText, JTextField::setText, ViewCampaign::getSystem, ViewCampaign::setSystem),
		GM("GM", JTextField::new, JTextField::getText, JTextField::setText, ViewCampaign::getGm, ViewCampaign::setGm),
		COMPLETED("Completed?", TrinaryJComponent::new, TrinaryJComponent::getValue, TrinaryJComponent::setValue, ViewCampaign::getCompletedFlg,
				ViewCampaign::setCompletedFlg);

		private <T extends JComponent, O> CampaignUpdatePopupField(final String label, final Supplier<T> fieldConstructor,
				final Function<T, O> fieldGetter, final BiConsumer<T, O> fieldSetter, final Function<ViewCampaign, O> entityGetter,
				final BiConsumer<ViewCampaign, O> entitySetter) {

			this.label = label;
			this.fieldConstructor = (Supplier<JComponent>) fieldConstructor;
			this.fieldGetter = (Function<JComponent, Object>) fieldGetter;
			this.fieldSetter = (BiConsumer<JComponent, Object>) fieldSetter;
			this.entityGetter = (Function<ViewCampaign, Object>) entityGetter;
			this.entitySetter = (BiConsumer<ViewCampaign, Object>) entitySetter;
		}

		private final String label;
		private final Supplier<JComponent> fieldConstructor;
		private final Function<JComponent, Object> fieldGetter;
		private final BiConsumer<JComponent, Object> fieldSetter;
		private final Function<ViewCampaign, Object> entityGetter;
		private final BiConsumer<ViewCampaign, Object> entitySetter;

		@Override
		public JComponent instantiateField(final ViewCampaign existing) {
			final JComponent field = fieldConstructor.get();
			fieldSetter.accept(field, entityGetter.apply(existing));
			return field;
		}

		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public void pullFromField(final JComponent field, final ViewCampaign toFill) {
			entitySetter.accept(toFill, fieldGetter.apply(field));
		}
	}
}
