package subway.domain;

import org.springframework.stereotype.Component;
import subway.global.exception.pricepolicy.CanNotDistanceEqualZeroException;

@Component
public class DefaultPricePolicy implements SubwayPricePolicy {

    private static final int DEFAULT_PRICE = 1250;
    private static final int DEFAULT_DISTANCE = 10;

    private static final int MID_DISTANCE = 40;
    private static final int MID_DISTANCE_RATE = 5;

    private static final int LONG_DISTANCE = 50;
    private static final int LONG_DISTANCE_RATE = 8;

    private static final int ADDITIONAL_FEE = 100;

    @Override
    public int calculate(final int distance) {
        validateDistance(distance);

        if (isDefaultDistance(distance)) {
            return DEFAULT_PRICE;
        }

        if (isLongDistance(distance)) {
            return DEFAULT_PRICE +
                    calculateMidDistance(MID_DISTANCE) +
                    calculateLongDistance(distance - MID_DISTANCE - DEFAULT_DISTANCE);
        }

        return DEFAULT_PRICE + calculateMidDistance(distance - DEFAULT_DISTANCE);
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new CanNotDistanceEqualZeroException("목적지에 갈 수 없습니다.");
        }
    }

    private boolean isDefaultDistance(final int distance) {
        return distance <= DEFAULT_DISTANCE;
    }

    private boolean isLongDistance(final int distance) {
        return distance > LONG_DISTANCE;
    }

    private int calculateMidDistance(final int distance) {
        return calculatePriceBracket(distance, MID_DISTANCE_RATE);
    }

    private int calculateLongDistance(final int distance) {
        return calculatePriceBracket(distance, LONG_DISTANCE_RATE);
    }

    private int calculatePriceBracket(final int distance, final int rate) {
        return (((distance - 1) / rate) + 1) * ADDITIONAL_FEE;
    }
}
