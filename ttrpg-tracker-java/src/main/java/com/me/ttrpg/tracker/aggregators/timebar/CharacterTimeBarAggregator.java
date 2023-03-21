package com.me.ttrpg.tracker.aggregators.timebar;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;
import com.me.ttrpg.tracker.dtos.entities.TtrpgSession;
import com.me.ttrpg.tracker.utils.AggregationUtils;

@Component
public class CharacterTimeBarAggregator extends AbstractTimeBarAggregator<TtrpgCharacter, TtrpgCharacter> {

	private static final Function<TtrpgCharacter, String> CHA_RACE_FUNC = ch -> ch.getRace();
	private static final Function<TtrpgCharacter, String> CHA_CLASS_FUNC = ch -> ch.getClassRole();
	private static final Function<TtrpgCharacter, String> CHA_GENDER_FUNC = ch -> ch.getGender();
	private static final Function<TtrpgCharacter, String> CHA_DIED_FUNC = ch -> AggregationUtils.convertBooleanToWord(ch.getDiedInGameFlg());
	private static final Function<TtrpgCharacter, String> CHA_TRAGIC_FUNC = ch -> AggregationUtils.convertBooleanToWord(ch.getTragicStoryFlg());

	@Override
	protected void addToList(final List<AbstractTimeBarAggregator<TtrpgCharacter, TtrpgCharacter>.InternalPojo> aggMap) {
		aggMap.add(new InternalPojo("Race", CHA_RACE_FUNC, "Character", CHA_NAME_FUNC, "Gender", CHA_GENDER_FUNC));
		aggMap.add(new InternalPojo("Race", CHA_RACE_FUNC, "Character", CHA_NAME_FUNC, "Died", CHA_DIED_FUNC));
		aggMap.add(new InternalPojo("Race", CHA_RACE_FUNC, "Character", CHA_NAME_FUNC, "Tragic", CHA_TRAGIC_FUNC));
		aggMap.add(new InternalPojo("Class", CHA_CLASS_FUNC, "Character", CHA_NAME_FUNC, "Gender", CHA_GENDER_FUNC));
		aggMap.add(new InternalPojo("Class", CHA_CLASS_FUNC, "Character", CHA_NAME_FUNC, "Died", CHA_DIED_FUNC));
		aggMap.add(new InternalPojo("Class", CHA_CLASS_FUNC, "Character", CHA_NAME_FUNC, "Tragic", CHA_TRAGIC_FUNC));
		aggMap.add(new InternalPojo("Gender", CHA_GENDER_FUNC, "Character", CHA_NAME_FUNC, "Died", CHA_DIED_FUNC));
		aggMap.add(new InternalPojo("Gender", CHA_GENDER_FUNC, "Character", CHA_NAME_FUNC, "Tragic", CHA_TRAGIC_FUNC));
		aggMap.add(new InternalPojo("Died", CHA_DIED_FUNC, "Character", CHA_NAME_FUNC, "Gender", CHA_GENDER_FUNC));
		aggMap.add(new InternalPojo("Died", CHA_DIED_FUNC, "Character", CHA_NAME_FUNC, "Tragic", CHA_TRAGIC_FUNC));
		aggMap.add(new InternalPojo("Tragic", CHA_TRAGIC_FUNC, "Character", CHA_NAME_FUNC, "Gender", CHA_GENDER_FUNC));
		aggMap.add(new InternalPojo("Tragic", CHA_TRAGIC_FUNC, "Character", CHA_NAME_FUNC, "Died", CHA_DIED_FUNC));
	}

	@Override
	protected Stream<TtrpgCharacter> rStream(final TtrpgCampaign campaign) {
		return campaign.getCharactersIPlayed().stream();
	}

	@Override
	protected Stream<TtrpgCharacter> bStream(final TtrpgCharacter rowElement) {
		return Stream.of(rowElement);
	}

	@Override
	protected Stream<TtrpgSession> sessionStream(final TtrpgCharacter element) {
		return element.getSessionsIPlayed().stream();
	}

}
