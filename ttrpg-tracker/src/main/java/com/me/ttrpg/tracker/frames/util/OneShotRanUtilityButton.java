package com.me.ttrpg.tracker.frames.util;

import org.springframework.stereotype.Component;

@Component
public class OneShotRanUtilityButton extends AbstractOneShotUtilityButton {

	@Override
	public String name() {
		return "Ran One Shot";
	}

	@Override
	protected boolean includeCharacter() {
		return false;
	}

}
