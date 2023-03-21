package com.me.ttrpg.tracker.dtos.view;

import java.time.LocalDate;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.me.gui.swing.views.crud.AbstractCrudPojo;
import com.me.ttrpg.tracker.dtos.entities.TtrpgSession;

public class ViewSession extends AbstractCrudPojo {

	private final ViewCharacter parentCharacter;
	private final ViewCampaign parentCampaign;

	private LocalDate playDate;
	private Boolean shortFlg;
	private Boolean played;

	public ViewSession(final TtrpgSession other, final ViewCharacter parentCharacter, final ViewCampaign parentCampaign, final boolean played) {
		super(other.getId());
		this.parentCharacter = parentCharacter;
		this.parentCampaign = parentCampaign;

		this.playDate = other.getPlayDate();
		this.shortFlg = other.getShortFlg();
		this.played = played;
	}

	public ViewSession(final String id, final ViewCharacter parentCharacter, final ViewCampaign parentCampaign) {
		super(id);
		this.parentCharacter = parentCharacter;
		this.parentCampaign = parentCampaign;
	}

	public ViewCharacter getParentCharacter() {
		return parentCharacter;
	}

	public ViewCampaign getParentCampaign() {
		return parentCampaign;
	}

	public LocalDate getPlayDate() {
		return playDate;
	}

	public void setPlayDate(final LocalDate playDate) {
		this.playDate = playDate;
	}

	public Boolean getShortFlg() {
		return shortFlg;
	}

	public void setShortFlg(final Boolean shortFlg) {
		this.shortFlg = shortFlg;
	}

	public Boolean getPlayed() {
		return played;
	}

	public void setPlayed(final Boolean played) {
		this.played = played;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this);
	}
}
