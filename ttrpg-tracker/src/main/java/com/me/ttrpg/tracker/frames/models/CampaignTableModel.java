package com.me.ttrpg.tracker.frames.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import com.me.gui.swing.views.crud.AbstractCrudTableModel;
import com.me.ttrpg.tracker.dtos.view.ViewCampaign;
import com.me.ttrpg.tracker.dtos.view.ViewCharacter;
import com.me.ttrpg.tracker.dtos.view.ViewSession;

public class CampaignTableModel extends AbstractCrudTableModel<ViewCampaign> {

	private static final long serialVersionUID = -468610370881212598L;
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

	@Override
	protected List<ColumnSpecifier<ViewCampaign>> getColumns() {
		return Arrays.asList(CampaignTableColumn.values());
	}

	@Override
	public int selectIndex() {
		return CampaignTableColumn.SELECT.ordinal();
	}

	@Override
	public int editIndex() {
		return CampaignTableColumn.EDIT.ordinal();
	}

	@Override
	public int deleteIndex() {
		return CampaignTableColumn.DELETE.ordinal();
	}

	@Override
	public int defaultSortIndex() {
		return CampaignTableColumn.LAST_PLAY.ordinal();
	}

	private enum CampaignTableColumn implements ColumnSpecifier<ViewCampaign> {
		SELECT("SELECT"),
		NAME(e -> e.getName(), String.class, "Name"),
		SYSTEM(e -> e.getSystem(), String.class, "System"),
		GM(e -> e.getGm(), String.class, "GM"),
		COMPLETED(e -> e.getCompletedFlg() == null ? "ongoing" : e.getCompletedFlg() ? "completed" : "abandoned", String.class, "Status"),
		FIRST_PLAY(CampaignTableColumn::firstPlayDate, String.class, "First Played"),
		LAST_PLAY(CampaignTableColumn::lastPlayDate, String.class, "Last Played"),
		EDIT("EDIT"),
		DELETE("DELETE");

		private CampaignTableColumn(final String buttonName) {
			this(e -> buttonName, String.class, "", true);
		}

		private <T> CampaignTableColumn(final Function<ViewCampaign, T> valueFunc, final Class<T> returnClass, final String name) {
			this(valueFunc, returnClass, name, false);
		}

		private CampaignTableColumn(final Function<ViewCampaign, ?> valueFunc, final Class<?> returnClass, final String name,
				final boolean editable) {
			this.valueFunc = (Function<ViewCampaign, Object>) valueFunc;
			this.returnClass = (Class<Object>) returnClass;
			this.name = name;
			this.editable = editable;
		}

		Function<ViewCampaign, Object> valueFunc;
		Class<Object> returnClass;
		String name;
		boolean editable;

		private static String firstPlayDate(final ViewCampaign campaign) {
			final Stream<ViewSession> characterSessions = campaign.getCharactersIPlayed().stream().map(ViewCharacter::getSessionsIPlayed)
					.flatMap(List::stream);
			final Stream<ViewSession> playedSessions = Stream.concat(characterSessions, campaign.getSessionsIPlayedNoCharacter().stream());
			final LocalDate max = Stream.concat(playedSessions, campaign.getSessionsIRan().stream()).map(ViewSession::getPlayDate)
					.min(LocalDate::compareTo).orElse(null);
			if(max == null) {
				return "NO DATA";
			}
			return max.format(DATE_FORMAT);
		}

		private static String lastPlayDate(final ViewCampaign campaign) {
			final Stream<ViewSession> characterSessions = campaign.getCharactersIPlayed().stream().map(ViewCharacter::getSessionsIPlayed)
					.flatMap(List::stream);
			final Stream<ViewSession> playedSessions = Stream.concat(characterSessions, campaign.getSessionsIPlayedNoCharacter().stream());
			final LocalDate max = Stream.concat(playedSessions, campaign.getSessionsIRan().stream()).map(ViewSession::getPlayDate)
					.max(LocalDate::compareTo).orElse(null);
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
		public Object getValue(final ViewCampaign p) {
			return valueFunc.apply(p);
		}

		@Override
		public boolean isEditable() {
			return editable;
		}
	}

}
