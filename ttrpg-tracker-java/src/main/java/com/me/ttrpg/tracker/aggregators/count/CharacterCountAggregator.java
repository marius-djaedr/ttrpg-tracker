package com.me.ttrpg.tracker.aggregators.count;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;

@Component
public class CharacterCountAggregator extends AbstractCharacterCountAggregator {

	@Override
	protected String nameFromCharacter(final TtrpgCharacter character) {
		return character.getName();
	}

	@Override
	protected String title() {
		return "Character";
	}

	@Override
	protected List<InternalChart> chartsToMake() {
		return Arrays.asList(InternalChart.SESSION_BAR);
	}
}
