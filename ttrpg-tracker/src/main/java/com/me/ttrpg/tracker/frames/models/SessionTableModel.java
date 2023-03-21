package com.me.ttrpg.tracker.frames.models;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.me.gui.swing.views.crud.AbstractCrudTableModel;
import com.me.ttrpg.tracker.dtos.view.ViewCharacter;
import com.me.ttrpg.tracker.dtos.view.ViewSession;

public class SessionTableModel extends AbstractCrudTableModel<ViewSession> {

	private static final long serialVersionUID = -468610370881212598L;
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

	@Override
	protected List<ColumnSpecifier<ViewSession>> getColumns() {
		return Arrays.asList(SessionTableColumn.values());
	}

	@Override
	public int editIndex() {
		return SessionTableColumn.EDIT.ordinal();
	}

	@Override
	public int deleteIndex() {
		return SessionTableColumn.DELETE.ordinal();
	}

	@Override
	public int defaultSortIndex() {
		return SessionTableColumn.DATE.ordinal();
	}

	@Override
	public List<Integer> columnsToRemove() {
		return Arrays.asList(campaignIdIndex(), characterIdIndex());
	}

	public static int campaignIdIndex() {
		return SessionTableColumn.CAMPAIGN_ID.ordinal();
	}

	public static int characterIdIndex() {
		return SessionTableColumn.CHARACTER_ID.ordinal();
	}

	private enum SessionTableColumn implements ColumnSpecifier<ViewSession> {
		CAMPAIGN(SessionTableColumn::campaignName, String.class, "Campaign"),
		CHARACTER(SessionTableColumn::characterName, String.class, "Character"),
		PLAYED(e -> e.getPlayed() == null ? "ERR" : e.getPlayed() ? "Played" : "Ran", String.class, "Played/Ran"),
		DATE(e -> e.getPlayDate().format(DATE_FORMAT), String.class, "Date"),
		SHORT(e -> e.getShortFlg() == null ? "regular" : e.getShortFlg() ? "short" : "regular", String.class, "Session Length"),
		EDIT("EDIT"),
		DELETE("DELETE"),
		CAMPAIGN_ID(e -> e.getParentCampaign().getId(), String.class, "TODO HIDE THIS"),
		CHARACTER_ID(SessionTableColumn::characterId, String.class, "TODO HIDE THIS");

		private SessionTableColumn(final String buttonName) {
			this(e -> buttonName, String.class, "", true);
		}

		private <T> SessionTableColumn(final Function<ViewSession, T> valueFunc, final Class<T> returnClass, final String name) {
			this(valueFunc, returnClass, name, false);
		}

		private SessionTableColumn(final Function<ViewSession, ?> valueFunc, final Class<?> returnClass, final String name, final boolean editable) {
			this.valueFunc = (Function<ViewSession, Object>) valueFunc;
			this.returnClass = (Class<Object>) returnClass;
			this.name = name;
			this.editable = editable;
		}

		Function<ViewSession, Object> valueFunc;
		Class<Object> returnClass;
		String name;
		boolean editable;

		private static String campaignName(final ViewSession session) {
			return session.getParentCampaign().getName();
		}

		private static String characterName(final ViewSession session) {
			final ViewCharacter parentCharacter = session.getParentCharacter();
			return parentCharacter == null ? "N/A" : parentCharacter.getName();
		}

		private static String characterId(final ViewSession session) {
			final ViewCharacter parentCharacter = session.getParentCharacter();
			return parentCharacter == null ? "N/A" : parentCharacter.getId();
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
		public Object getValue(final ViewSession p) {
			return valueFunc.apply(p);
		}

		@Override
		public boolean isEditable() {
			return editable;
		}
	}

}
