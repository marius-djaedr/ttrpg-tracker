package com.me.ttrpg.tracker.aggregators.count;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;

@Component
public class CharacterGenderCountAggregator extends AbstractCharacterCountAggregator {

	@Override
	protected String nameFromCharacter(final TtrpgCharacter character) {
		return character.getGender();
	}

	@Override
	protected String title() {
		return "Character - Gender";
	}

	@Override
	protected List<InternalChart> chartsToMake() {
		return Arrays.asList(InternalChart.SESSION_PIE, InternalChart.CHARACTER_PIE);
	}
}
