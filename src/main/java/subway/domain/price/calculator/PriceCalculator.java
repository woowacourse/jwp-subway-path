package subway.domain.price.calculator;

import subway.domain.Distance;
import subway.domain.price.Price;

public interface PriceCalculator {

    Price calculateFare(final Distance distance);
}
