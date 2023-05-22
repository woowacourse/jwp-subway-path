package subway.domain.fare.normal;

import static subway.domain.fare.normal.FareDistanceType.EXTRA_FARE_DISTANCE_FIFTY;
import static subway.domain.fare.normal.FareDistanceType.EXTRA_FARE_DISTANCE_TEN;
import static subway.domain.fare.normal.FareDistanceType.FARE_DISTANCE_UNIT_EIGHT;
import static subway.domain.fare.normal.FareDistanceType.FARE_DISTANCE_UNIT_FIVE;
import static subway.domain.fare.normal.FareType.BASIC_FARE;
import static subway.domain.fare.normal.FareType.EXTRA_FARE;

import org.springframework.stereotype.Component;
import subway.domain.fare.Fare;
import subway.domain.route.Distance;

@Component
public class UnitEightFarePolicy implements FarePolicy {

    @Override
    public boolean isAvailable(final Distance distance) {
        return distance.moreThan(EXTRA_FARE_DISTANCE_FIFTY.distance());
    }

    @Override
    public Fare calculateFare(final Distance distance) {
        final Distance fareSectionDistance = EXTRA_FARE_DISTANCE_FIFTY.distance()
            .subtract(EXTRA_FARE_DISTANCE_TEN.distance());
        final Distance extraDistance = distance.subtract(EXTRA_FARE_DISTANCE_FIFTY.distance());
        final Fare extraFare = calculateByDistance(fareSectionDistance, FARE_DISTANCE_UNIT_FIVE.distance())
            .add(calculateByDistance(extraDistance, FARE_DISTANCE_UNIT_EIGHT.distance()));
        return BASIC_FARE.fare().add(extraFare);
    }

    private Fare calculateByDistance(final Distance distance, final Distance unitDistance) {
        final Distance dividedDistance = distance.divideAndCeil(unitDistance);
        return new Fare(dividedDistance.distance()).multiply(EXTRA_FARE.fare());
    }
}
