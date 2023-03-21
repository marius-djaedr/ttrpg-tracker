package com.me.ttrpg.tracker.aggregators.count;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;
import com.me.ttrpg.tracker.utils.AggregationUtils;

public abstract class AbstractCharacterCountAggregator extends AbstractCountAggregator {

	@Override
	public void processCampaign(final TtrpgCampaign element) {
		for(final TtrpgCharacter character : element.getCharactersIPlayed()) {
			final String toSplit = nameFromCharacter(character);
			final List<String> keyList = toSplit == null ? Collections.singletonList(null) : Arrays.asList(toSplit.split("/"));
			final BigDecimal weightInverse = BigDecimal.ONE.setScale(10).divide(BigDecimal.valueOf(keyList.size()).setScale(10),
					RoundingMode.HALF_UP);
			for(final String key : keyList) {
				AggregationUtils.increment(characterMap, key, weightInverse);

				final BigDecimal sessions = AggregationUtils.numberSessions(character);
				final BigDecimal toAdd = sessions.setScale(10).multiply(weightInverse);
				AggregationUtils.increment(sessionMap, key, toAdd);
			}
		}
	}

	protected abstract String nameFromCharacter(TtrpgCharacter character);

}
