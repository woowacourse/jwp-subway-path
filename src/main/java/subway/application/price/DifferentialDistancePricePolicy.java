package subway.application.price;

import static subway.application.price.DifferentialDistancePricePolicy.DistanceLevel.LEVEL1;
import static subway.application.price.DifferentialDistancePricePolicy.DistanceLevel.LEVEL2;
import static subway.application.price.DifferentialDistancePricePolicy.DistanceLevel.LEVEL3;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.IntToLongFunction;
import subway.domain.path.Path;
import subway.domain.price.Price;

public class DifferentialDistancePricePolicy implements PricePolicy {
    private static final int DEFAULT_PRICE = 1250;
    private static final int EXTRA_PRICE_RATE = 100;
    private static final Map<DistanceLevel, IntToLongFunction> DISTANCE_LEVEL_MAP = new EnumMap<>(
            DistanceLevel.class);

    static {
        DISTANCE_LEVEL_MAP.put(LEVEL1, DifferentialDistancePricePolicy::distanceLevel1Calculate);
        DISTANCE_LEVEL_MAP.put(LEVEL2, DifferentialDistancePricePolicy::distanceLevel2Calculate);
        DISTANCE_LEVEL_MAP.put(LEVEL3, DifferentialDistancePricePolicy::distanceLevel3Calculate);
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
    public Price calculate(Path path) {
        int distance = path.getTotalDistance();
        DistanceLevel distanceLevel = DistanceLevel.from(distance);
        long price = DISTANCE_LEVEL_MAP.get(distanceLevel).applyAsLong(distance);
        return Price.from(price);
    }

    enum DistanceLevel {
        LEVEL1(1, 10, 0),
        LEVEL2(11, 50, 5),
        LEVEL3(51, Integer.MAX_VALUE, 8);

        DistanceLevel(int minDistance, int maxDistance, int levelPerDistance) {
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.levelPerDistance = levelPerDistance;
        }

        private final int minDistance;
        private final int maxDistance;
        private final int levelPerDistance;

        public static DistanceLevel from(int distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("거리는 1 이상의 값이어야 합니다.");
            }
            if (distance <= LEVEL1.maxDistance) {
                return LEVEL1;
            }
            if (distance <= LEVEL2.maxDistance) {
                return LEVEL2;
            }
            return LEVEL3;
        }

        public int getMinDistance() {
            return minDistance;
        }

        public int getMaxDistance() {
            return maxDistance;
        }

        public int getLevelPerDistance() {
            return levelPerDistance;
        }
    }
}
