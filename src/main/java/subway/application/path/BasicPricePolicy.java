package subway.application.path;

import static subway.application.path.DistanceLevel.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;

public class BasicPricePolicy implements PricePolicy {
    private static final int DEFAULT_PRICE = 1250;
    private static final int EXTRA_PRICE_RATE = 100;
    private static final Map<DistanceLevel, IntUnaryOperator> DISTANCE_LEVEL_MAP = new EnumMap<>(
            DistanceLevel.class);

    static {
        DISTANCE_LEVEL_MAP.put(LEVEL1, BasicPricePolicy::distanceLevel1Calculate);
        DISTANCE_LEVEL_MAP.put(LEVEL2, BasicPricePolicy::distanceLevel2Calculate);
        DISTANCE_LEVEL_MAP.put(LEVEL3, BasicPricePolicy::distanceLevel3Calculate);
    }

    private static int distanceLevel1Calculate(int distance) {
        return DEFAULT_PRICE;
    }

    private static int distanceLevel2Calculate(int distance) {
        return distanceLevel1Calculate(LEVEL1.getMaxDistance())
                + ((distance - LEVEL2.getMinDistance()) / LEVEL2.getLevelPerDistance() + 1)
                * EXTRA_PRICE_RATE;
    }

    private static int distanceLevel3Calculate(int distance) {
        return distanceLevel2Calculate(LEVEL2.getMaxDistance())
                + ((distance - LEVEL3.getMinDistance()) / LEVEL3.getLevelPerDistance() + 1)
                * EXTRA_PRICE_RATE;
    }

    @Override
    public int calculate(int distance) {
        DistanceLevel distanceLevel = from(distance);
        return DISTANCE_LEVEL_MAP.get(distanceLevel).applyAsInt(distance);
    }
}
