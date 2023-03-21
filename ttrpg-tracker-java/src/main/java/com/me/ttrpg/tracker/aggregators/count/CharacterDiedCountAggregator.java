package com.me.ttrpg.tracker.aggregators.count;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;
import com.me.ttrpg.tracker.utils.AggregationUtils;

@Component
public class CharacterDiedCountAggregator extends AbstractCharacterCountAggregator {

	@Override
	protected String nameFromCharacter(final TtrpgCharacter character) {
		return AggregationUtils.convertBooleanToWord(character.getDiedInGameFlg());
	}

	@Override
	protected String title() {
		return "Character - Died in Game";
	}

	@Override
	protected List<InternalChart> chartsToMake() {
		return Arrays.asList(InternalChart.CHARACTER_PIE);
	}
}
