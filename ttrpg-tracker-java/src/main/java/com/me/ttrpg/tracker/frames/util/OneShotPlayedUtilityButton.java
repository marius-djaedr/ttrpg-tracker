package com.me.ttrpg.tracker.frames.util;

import org.springframework.stereotype.Component;

@Component
public class OneShotPlayedUtilityButton extends AbstractOneShotUtilityButton {

	@Override
	public String name() {
		return "Played One Shot";
	}

	@Override
	protected boolean includeCharacter() {
		return true;
	}

}
