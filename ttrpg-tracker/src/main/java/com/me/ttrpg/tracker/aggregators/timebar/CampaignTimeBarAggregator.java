package com.me.ttrpg.tracker.aggregators.timebar;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;
import com.me.ttrpg.tracker.dtos.entities.TtrpgSession;
import com.me.ttrpg.tracker.utils.AggregationUtils;

@Component
public class CampaignTimeBarAggregator extends AbstractTimeBarAggregator<TtrpgCampaign, TtrpgCampaign> {
	private static final Function<TtrpgCampaign, String> COMPLETE_FUNC = ca -> AggregationUtils.convertBooleanToWord(ca.getCompletedFlg());

	@Override
	protected void addToList(final List<AbstractTimeBarAggregator<TtrpgCampaign, TtrpgCampaign>.InternalPojo> aggMap) {
		aggMap.add(new InternalPojo("System", SYS_FUNC, "GM", GM_FUNC));
		aggMap.add(new InternalPojo("System", SYS_FUNC, "Campaign", NAME_FUNC, "Completed", COMPLETE_FUNC));
		aggMap.add(new InternalPojo("GM", GM_FUNC, "System", SYS_FUNC));
		aggMap.add(new InternalPojo("GM", GM_FUNC, "Campaign", NAME_FUNC, "Completed", COMPLETE_FUNC));
	}

	@Override
	protected Stream<TtrpgCampaign> rStream(final TtrpgCampaign campaign) {
		return Stream.of(campaign);
	}

	@Override
	protected Stream<TtrpgCampaign> bStream(final TtrpgCampaign rowElement) {
		return Stream.of(rowElement);
	}

	@Override
	protected Stream<TtrpgSession> sessionStream(final TtrpgCampaign element) {
		return Stream.concat(Stream.concat(element.getSessionsIRan().stream(), element.getSessionsIPlayedNoCharacter().stream()),
				element.getCharactersIPlayed().stream().map(TtrpgCharacter::getSessionsIPlayed).flatMap(Collection::stream));
	}

}
