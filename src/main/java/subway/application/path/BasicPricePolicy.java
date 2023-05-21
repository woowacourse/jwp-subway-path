package subway.application.path;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;

public class BasicPricePolicy implements PricePolicy {
    private static final int DEFAULT_PRICE = 1250;
    private static final int LEVEL2_MAX_PRICE = 2050;
    private static final int EXTRA_PRICE_RATE = 100;
    private static final int LEVEL1_THRESHOLD = 11;
    private static final int LEVEL2_THRESHOLD = 51;
    private static final int LEVEL2_PER_DISTANCE = 5;
    private static final int LEVEL3_PER_DISTANCE = 8;
    private static final Map<DistanceLevel, IntUnaryOperator> DISTANCE_LEVEL_MAP = new EnumMap<>(
            DistanceLevel.class);

    static {
        DISTANCE_LEVEL_MAP.put(DistanceLevel.LEVEL1, distance -> DEFAULT_PRICE);
        DISTANCE_LEVEL_MAP.put(DistanceLevel.LEVEL2, distance ->
                DEFAULT_PRICE + ((distance - LEVEL1_THRESHOLD) / LEVEL2_PER_DISTANCE + 1) * EXTRA_PRICE_RATE);
        DISTANCE_LEVEL_MAP.put(DistanceLevel.LEVEL3, distance ->
                LEVEL2_MAX_PRICE + ((distance - LEVEL2_THRESHOLD) / LEVEL3_PER_DISTANCE + 1) * EXTRA_PRICE_RATE);
    }

    @Override
    public int calculate(int distance) {
        DistanceLevel distanceLevel = DistanceLevel.from(distance);
        return DISTANCE_LEVEL_MAP.get(distanceLevel).applyAsInt(distance);
    }
}
