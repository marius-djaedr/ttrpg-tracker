package com.me.ttrpg.tracker.aggregators.timebar;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;
import com.me.ttrpg.tracker.dtos.entities.TtrpgSession;

@Component
public class CampaignCharacterTimeBarAggregator extends AbstractTimeBarAggregator<TtrpgCampaign, TtrpgCharacter> {

	@Override
	protected void addToList(final List<AbstractTimeBarAggregator<TtrpgCampaign, TtrpgCharacter>.InternalPojo> aggMap) {
		aggMap.add(new InternalPojo("System", SYS_FUNC, "Character", CHA_NAME_FUNC));
		aggMap.add(new InternalPojo("GM", GM_FUNC, "Character", CHA_NAME_FUNC));
		aggMap.add(new InternalPojo("Campaign", NAME_FUNC, "Character", CHA_NAME_FUNC));
	}

	@Override
	protected Stream<TtrpgCampaign> rStream(final TtrpgCampaign campaign) {
		return Stream.of(campaign);
	}

	@Override
	protected Stream<TtrpgCharacter> bStream(final TtrpgCampaign rowElement) {
		return rowElement.getCharactersIPlayed().stream();
	}

	@Override
	protected Stream<TtrpgSession> sessionStream(final TtrpgCharacter element) {
		return element.getSessionsIPlayed().stream();
	}

}
