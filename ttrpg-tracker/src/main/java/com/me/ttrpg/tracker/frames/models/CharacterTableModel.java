package com.me.ttrpg.tracker.frames.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.me.gui.swing.views.crud.AbstractCrudTableModel;
import com.me.ttrpg.tracker.dtos.view.ViewCharacter;
import com.me.ttrpg.tracker.dtos.view.ViewSession;

public class CharacterTableModel extends AbstractCrudTableModel<ViewCharacter> {

	private static final long serialVersionUID = -468610370881212598L;
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

	@Override
	protected List<ColumnSpecifier<ViewCharacter>> getColumns() {
		return Arrays.asList(CharacterTableColumn.values());
	}

	@Override
	public int selectIndex() {
		return CharacterTableColumn.SELECT.ordinal();
	}

	@Override
	public int editIndex() {
		return CharacterTableColumn.EDIT.ordinal();
	}

	@Override
	public int deleteIndex() {
		return CharacterTableColumn.DELETE.ordinal();
	}

	@Override
	public int defaultSortIndex() {
		return CharacterTableColumn.LAST_PLAY.ordinal();
	}

	@Override
	public List<Integer> columnsToRemove() {
		return Arrays.asList(campaignIdIndex());
	}

	public static int campaignIdIndex() {
		return CharacterTableColumn.CAMPAIGN_ID.ordinal();
	}

	private enum CharacterTableColumn implements ColumnSpecifier<ViewCharacter> {
		SELECT("SELECT"),
		CAMPAIGN(e -> e.getParentCampaign().getName(), String.class, "Campaign"),
		NAME(e -> e.getName(), String.class, "Name"),
		RACE(e -> e.getRace(), String.class, "Race"),
		CLASS(e -> e.getClassRole(), String.class, "Class/Role"),
		GENDER(e -> e.getGender(), String.class, "Gender"),
		TRAGIC(e -> e.getTragicStoryFlg() == null ? "unknown" : e.getTragicStoryFlg() ? "tragic" : "pleasant", String.class, "Tragic Backstory?"),
		DIED(e -> e.getDiedInGameFlg() == null ? "unknown" : e.getDiedInGameFlg() ? "died" : "lived", String.class, "Died In Game?"),
		LAST_PLAY(CharacterTableColumn::lastPlayDate, String.class, "Last Played"),
		EDIT("EDIT"),
		DELETE("DELETE"),
		CAMPAIGN_ID(e -> e.getParentCampaign().getId(), String.class, "TODO HIDE THIS");

		private CharacterTableColumn(final String buttonName) {
			this(e -> buttonName, String.class, "", true);
		}

		private <T> CharacterTableColumn(final Function<ViewCharacter, T> valueFunc, final Class<T> returnClass, final String name) {
			this(valueFunc, returnClass, name, false);
		}

		private CharacterTableColumn(final Function<ViewCharacter, ?> valueFunc, final Class<?> returnClass, final String name,
				final boolean editable) {
			this.valueFunc = (Function<ViewCharacter, Object>) valueFunc;
			this.returnClass = (Class<Object>) returnClass;
			this.name = name;
			this.editable = editable;
		}

		Function<ViewCharacter, Object> valueFunc;
		Class<Object> returnClass;
		String name;
		boolean editable;

		private static String lastPlayDate(final ViewCharacter character) {
			final LocalDate max = character.getSessionsIPlayed().stream().map(ViewSession::getPlayDate).max(LocalDate::compareTo).orElse(null);
			if(max == null) {
				return "NO DATA";
			}
			return max.format(DATE_FORMAT);
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Class<?> getValueType() {
			return returnClass;
		}

		@Override
		public Object getValue(final ViewCharacter p) {
			return valueFunc.apply(p);
		}

		@Override
		public boolean isEditable() {
			return editable;
		}
	}

}
