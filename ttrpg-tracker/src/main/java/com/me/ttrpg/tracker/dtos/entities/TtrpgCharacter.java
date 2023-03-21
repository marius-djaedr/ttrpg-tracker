package com.me.ttrpg.tracker.dtos.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class TtrpgCharacter extends AbstractEntity {

	private String name;
	private String race;
	private String classRole;
	private String gender;
	private Boolean tragicStoryFlg;
	private Boolean diedInGameFlg;

	private final Map<String, TtrpgSession> sessionsIPlayed = new HashMap<>();

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

	public Collection<TtrpgSession> getSessionsIPlayed() {
		return sessionsIPlayed.values();
	}

	public TtrpgSession getSessionIPlayed(final String id) {
		return sessionsIPlayed.get(id);
	}

	public void addSessionIPlayed(final TtrpgSession session) {
		sessionsIPlayed.put(session.getId(), session);
	}

	public void removeSessionIPlayed(final String id) {
		sessionsIPlayed.remove(id);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "sessionsIPlayed");
	}
}
