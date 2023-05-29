package subway.domain.price.calculator;

import subway.domain.Distance;
import subway.domain.price.Price;

public class BasicPriceCalculator implements PriceCalculator {

    private static final Price BASIC_FARE = Price.from(1250);

    @Override
    public Price calculateFare(final Distance distance) {

        return BASIC_FARE;
    }
}
