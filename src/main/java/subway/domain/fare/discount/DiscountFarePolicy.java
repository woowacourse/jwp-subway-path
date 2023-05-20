package subway.domain.fare.discount;

import subway.domain.fare.Fare;

public interface DiscountFarePolicy {

    Fare calculateFare(final Fare basicFare);
}
