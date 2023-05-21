package subway.domain.subway.discount_policy;

import subway.domain.passenger.Passenger;
import subway.domain.subway.billing_policy.Fare;

public interface DiscountPolicy {

    Fare calculateDiscountedFare(Fare fare, Passenger passenger);

    Priority getPriority();
}
