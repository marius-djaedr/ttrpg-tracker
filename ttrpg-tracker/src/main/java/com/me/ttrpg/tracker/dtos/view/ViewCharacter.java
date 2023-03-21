package com.me.ttrpg.tracker.dtos.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.me.gui.swing.views.crud.AbstractCrudPojo;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;

public class ViewCharacter extends AbstractCrudPojo {

	private final ViewCampaign parentCampaign;

	private String name;
	private String race;
	private String classRole;
	private String gender;
	private Boolean tragicStoryFlg;
	private Boolean diedInGameFlg;

	private final List<ViewSession> sessionsIPlayed;

	public ViewCharacter(final TtrpgCharacter other, final ViewCampaign parentCampaign) {
		super(other.getId());
		this.parentCampaign = parentCampaign;

		this.name = other.getName();
		this.race = other.getRace();
		this.classRole = other.getClassRole();
		this.gender = other.getGender();
		this.tragicStoryFlg = other.getTragicStoryFlg();
		this.diedInGameFlg = other.getDiedInGameFlg();

		this.sessionsIPlayed = other.getSessionsIPlayed().stream().map(s -> new ViewSession(s, this, parentCampaign, true))
				.collect(Collectors.toList());
	}

	public ViewCharacter(final String id, final ViewCampaign parentCampaign) {
		super(id);
		this.parentCampaign = parentCampaign;
		this.sessionsIPlayed = new ArrayList<>();
	}

	public ViewCampaign getParentCampaign() {
		return parentCampaign;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getRace() {
		return race;
	}

	public void setRace(final String race) {
		this.race = race;
	}

	public String getClassRole() {
		return classRole;
	}

	public void setClassRole(final String classRole) {
		this.classRole = classRole;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}

	public Boolean getTragicStoryFlg() {
		return tragicStoryFlg;
	}

	public void setTragicStoryFlg(final Boolean tragicStoryFlg) {
		this.tragicStoryFlg = tragicStoryFlg;
	}

	public Boolean getDiedInGameFlg() {
		return diedInGameFlg;
	}

	public void setDiedInGameFlg(final Boolean diedInGameFlg) {
		this.diedInGameFlg = diedInGameFlg;
	}

	public List<ViewSession> getSessionsIPlayed() {
		return sessionsIPlayed;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "sessionsIPlayed");
	}
}
