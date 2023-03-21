package com.me.ttrpg.tracker.dtos.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class TtrpgCampaign extends AbstractEntity {

	private String name;
	private String system;
	private String gm;
	private Boolean completedFlg;

	private final Map<String, TtrpgCharacter> charactersIPlayed = new HashMap<>();
	private final Map<String, TtrpgSession> sessionsIPlayedNoCharacter = new HashMap<>();
	private final Map<String, TtrpgSession> sessionsIRan = new HashMap<>();

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(final String system) {
		this.system = system;
	}

	public String getGm() {
		return gm;
	}

	public void setGm(final String gm) {
		this.gm = gm;
	}

	public Boolean getCompletedFlg() {
		return completedFlg;
	}

	public void setCompletedFlg(final Boolean completedFlg) {
		this.completedFlg = completedFlg;
	}

	public Collection<TtrpgCharacter> getCharactersIPlayed() {
		return charactersIPlayed.values();
	}

	public TtrpgCharacter getCharacterIPlayed(final String id) {
		return charactersIPlayed.get(id);
	}

	public void addCharacterIPlayed(final TtrpgCharacter character) {
		charactersIPlayed.put(character.getId(), character);
	}

	public void removeCharacterIPlayed(final String id) {
		charactersIPlayed.remove(id);
	}

	public Collection<TtrpgSession> getSessionsIPlayedNoCharacter() {
		return sessionsIPlayedNoCharacter.values();
	}

	public TtrpgSession getSessionIPlayedNoCharacter(final String id) {
		return sessionsIPlayedNoCharacter.get(id);
	}

	public void addSessionIPlayedNoCharacter(final TtrpgSession session) {
		sessionsIPlayedNoCharacter.put(session.getId(), session);
	}

	public void removeSessionIPlayedNoCharacter(final String id) {
		sessionsIPlayedNoCharacter.remove(id);
	}

	public Collection<TtrpgSession> getSessionsIRan() {
		return sessionsIRan.values();
	}

	public TtrpgSession getSessionIRan(final String id) {
		return sessionsIRan.get(id);
	}

	public void addSessionIRan(final TtrpgSession session) {
		sessionsIRan.put(session.getId(), session);
	}

	public void removeSessionIRan(final String id) {
		sessionsIRan.remove(id);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "charactersIPlayed", "sessionsIRan");
	}
}
