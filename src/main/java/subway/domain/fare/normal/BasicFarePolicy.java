package subway.domain.fare.normal;

import static subway.domain.fare.normal.FareDistanceType.EXTRA_FARE_DISTANCE_TEN;
import static subway.domain.fare.normal.FareType.BASIC_FARE;

import org.springframework.stereotype.Component;
import subway.domain.fare.Fare;
import subway.domain.route.Distance;

@Component
public class BasicFarePolicy implements FarePolicy {

    @Override
    public boolean isAvailable(final Distance distance) {
        return distance.lessAndEqualsThan(EXTRA_FARE_DISTANCE_TEN.distance());
    }

    @Override
    public Fare calculateFare(final Distance distance) {
        return BASIC_FARE.fare();
    }
}
