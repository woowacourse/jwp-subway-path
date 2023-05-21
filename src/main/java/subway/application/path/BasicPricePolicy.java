package subway.application.path;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;

public class BasicPricePolicy implements PricePolicy {
    private static final int LEVEL1_PRICE = 1250;
    private static final int LEVEL2_MAX_PRICE = 2050;
    private static final int EXTRA_PRICE_RATE = 100;
    private static final int LEVEL1_THRESHOLD = 11;
    private static final int LEVEL2_THRESHOLD = 51;
    private static final int LEVEL2_PER_DISTANCE = 5;
    private static final int LEVEL3_PER_DISTANCE = 8;
    private static final Map<PriceLevel, IntUnaryOperator> PRICE_LEVEL_MAP = new EnumMap<>(PriceLevel.class);

    static {
        PRICE_LEVEL_MAP.put(PriceLevel.LEVEL1, distance -> LEVEL1_PRICE);
        PRICE_LEVEL_MAP.put(PriceLevel.LEVEL2, distance ->
                LEVEL1_PRICE + ((distance - LEVEL1_THRESHOLD) / LEVEL2_PER_DISTANCE + 1) * EXTRA_PRICE_RATE);
        PRICE_LEVEL_MAP.put(PriceLevel.LEVEL3, distance ->
                LEVEL2_MAX_PRICE + ((distance - LEVEL2_THRESHOLD) / LEVEL3_PER_DISTANCE + 1) * EXTRA_PRICE_RATE);
    }

    @Override
    public int calculate(int distance) {
        PriceLevel priceLevel = PriceLevel.from(distance);
        return PRICE_LEVEL_MAP.get(priceLevel).applyAsInt(distance);
    }
}
