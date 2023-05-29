package subway.domain.price.calculator;

import subway.domain.Distance;
import subway.domain.price.Price;

public class TenToFiftyPriceCalculator implements PriceCalculator {

    private static final Distance TEN_KM_DISTANCE = Distance.from(10);
    private static final Distance FIFTY_KM_DISTANCE = Distance.from(50);
    private static final Distance FARE_PER_DISTANCE = Distance.from(5);
    private static final Price NO_ADDITION_FARE = Price.from(0);
    private static final Price ADDITION_FARE = Price.from(100);

    @Override
    public Price calculateFare(final Distance distance) {
        if (distance.isLessThanOrEqualTo(TEN_KM_DISTANCE)) {
            return NO_ADDITION_FARE;
        }

        final Distance remainDistance = getDistanceForCalculating(distance);

        return Price.calculateSurcharge(remainDistance, FARE_PER_DISTANCE, ADDITION_FARE);
    }

    private Distance getDistanceForCalculating(final Distance distance) {
        if (distance.isMoreThan(FIFTY_KM_DISTANCE)) {
            return FIFTY_KM_DISTANCE.minus(TEN_KM_DISTANCE);
        }
        return distance.minus(TEN_KM_DISTANCE);
    }
}
