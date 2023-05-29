package subway.domain.price.calculator;

import subway.domain.Distance;
import subway.domain.price.Price;

public class OverFiftyPriceCalculator implements PriceCalculator {

    private static final Distance FIFTY_KM_DISTANCE = Distance.from(50);
    private static final Distance FARE_PER_DISTANCE = Distance.from(8);
    private static final Price NO_ADDITION_FARE = Price.from(0);
    private static final Price ADDITION_FARE = Price.from(100);

    @Override
    public Price calculateFare(final Distance distance) {
        if (distance.isLessThanOrEqualTo(FIFTY_KM_DISTANCE)) {
            return NO_ADDITION_FARE;
        }

        final Distance remainDistance = distance.minus(FIFTY_KM_DISTANCE);

        return Price.calculateSurcharge(remainDistance, FARE_PER_DISTANCE, ADDITION_FARE);
    }
}
