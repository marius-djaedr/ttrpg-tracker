package com.me.ttrpg.tracker.utils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import com.me.ttrpg.tracker.dtos.entities.TtrpgCampaign;
import com.me.ttrpg.tracker.dtos.entities.TtrpgCharacter;
import com.me.ttrpg.tracker.dtos.entities.TtrpgSession;

public class AggregationUtils {
	private static final BigDecimal ZERO = new BigDecimal("0").setScale(10);
	private static final BigDecimal ONE = new BigDecimal("1").setScale(10);
	private static final BigDecimal HALF = new BigDecimal("0.5").setScale(10);

	public static <K> void increment(final Map<K, BigDecimal> map, final K key) {
		increment(map, key, ONE);
	}

	public static <K> void increment(final Map<K, BigDecimal> map, final K key, final BigDecimal amount) {
		final BigDecimal current = map.getOrDefault(key, ZERO);
		map.put(key, current.add(amount));
	}

	public static BigDecimal numberSessions(final TtrpgCampaign campaign) {
		return numberSessions(campaign.getSessionsIRan().stream(), campaign.getSessionsIPlayedNoCharacter().stream(),
				campaign.getCharactersIPlayed().stream().map(TtrpgCharacter::getSessionsIPlayed).flatMap(Collection::stream));
	}

	public static BigDecimal numberSessions(final TtrpgCharacter character) {
		return numberSessions(character.getSessionsIPlayed().stream());
	}

	@SafeVarargs
	public static BigDecimal numberSessions(final Stream<TtrpgSession>... sessionStreams) {
		return Stream.of(sessionStreams).flatMap(s -> s).map(AggregationUtils::sessionValue).reduce(ZERO, BigDecimal::add);
	}

	public static BigDecimal sessionValue(final TtrpgSession session) {
		final Boolean isShort = session.getShortFlg();
		if(isShort == null || !isShort) {
			return ONE;
		} else {
			return HALF;
		}
	}

	public static String convertBooleanToWord(final Boolean bool) {
		if(bool == null) {
			return "Unknown";
		} else if(bool) {
			return "Yes";
		} else {
			return "No";
		}
	}
}
