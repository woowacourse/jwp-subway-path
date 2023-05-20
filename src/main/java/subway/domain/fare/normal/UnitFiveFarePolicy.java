package subway.domain.fare.normal;

import static subway.domain.fare.normal.FareDistanceType.EXTRA_FARE_DISTANCE_FIFTY;
import static subway.domain.fare.normal.FareDistanceType.EXTRA_FARE_DISTANCE_TEN;
import static subway.domain.fare.normal.FareDistanceType.FARE_DISTANCE_UNIT_FIVE;
import static subway.domain.fare.normal.FareType.BASIC_FARE;
import static subway.domain.fare.normal.FareType.EXTRA_FARE;

import org.springframework.stereotype.Component;
import subway.domain.fare.Fare;
import subway.domain.route.Distance;

@Component
public class UnitFiveFarePolicy implements FarePolicy {

    @Override
    public boolean isAvailable(final Distance distance) {
        return distance.moreThan(EXTRA_FARE_DISTANCE_TEN.distance())
            && distance.lessAndEqualsThan(EXTRA_FARE_DISTANCE_FIFTY.distance());
    }

    @Override
    public Fare calculateFare(final Distance distance) {
        final Distance extraDistance = distance.subtract(EXTRA_FARE_DISTANCE_TEN.distance());
        return BASIC_FARE.fare().add(calculateByDistance(extraDistance));
    }


    private Fare calculateByDistance(final Distance distance) {
        final Distance dividedDistance = distance.divideAndCeil(FARE_DISTANCE_UNIT_FIVE.distance());
        return new Fare(dividedDistance.distance()).multiply(EXTRA_FARE.fare());
    }
}
