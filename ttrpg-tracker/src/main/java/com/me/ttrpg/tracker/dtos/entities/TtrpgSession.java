package com.me.ttrpg.tracker.dtos.entities;

import java.time.LocalDate;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class TtrpgSession extends AbstractEntity {

	private LocalDate playDate;
	private Boolean shortFlg;

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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this);
	}
}
