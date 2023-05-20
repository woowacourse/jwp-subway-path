package subway.domain.fare.normal;

import subway.domain.fare.Fare;
import subway.domain.route.Distance;

public interface FarePolicy {

    boolean isAvailable(final Distance distance);

    Fare calculateFare(final Distance distance);
}
