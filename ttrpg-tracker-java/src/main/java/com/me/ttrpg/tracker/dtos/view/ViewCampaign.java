package com.me.ttrpg.tracker.dtos.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.me.gui.swing.views.crud.AbstractCrudPojo;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;

public class ViewCampaign extends AbstractCrudPojo {

	private String name;
	private String system;
	private String gm;
	private Boolean completedFlg;

	private final List<ViewCharacter> charactersIPlayed;
	private final List<ViewSession> sessionsIPlayedNoCharacter;
	private final List<ViewSession> sessionsIRan;

	public ViewCampaign(final TtrpgCampaign other) {
		super(other.getId());
		this.name = other.getName();
		this.system = other.getSystem();
		this.gm = other.getGm();
		this.completedFlg = other.getCompletedFlg();

		this.charactersIPlayed = other.getCharactersIPlayed().stream().map(ch -> new ViewCharacter(ch, this)).collect(Collectors.toList());
		this.sessionsIPlayedNoCharacter = other.getSessionsIPlayedNoCharacter().stream().map(s -> new ViewSession(s, null, this, true))
				.collect(Collectors.toList());
		this.sessionsIRan = other.getSessionsIRan().stream().map(s -> new ViewSession(s, null, this, false)).collect(Collectors.toList());
	}

	public ViewCampaign(final String id) {
		super(id);

		this.charactersIPlayed = new ArrayList<>();
		this.sessionsIPlayedNoCharacter = new ArrayList<>();
		this.sessionsIRan = new ArrayList<>();
	}

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

	public List<ViewCharacter> getCharactersIPlayed() {
		return charactersIPlayed;
	}

	public List<ViewSession> getSessionsIPlayedNoCharacter() {
		return sessionsIPlayedNoCharacter;
	}

	public List<ViewSession> getSessionsIRan() {
		return sessionsIRan;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, "charactersIPlayed", "sessionsIPlayedNoCharacter", "sessionsIRan");
	}
}
