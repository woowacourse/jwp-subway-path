package subway.domain.fare;

import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;

public class DistanceFareStrategy implements FareStrategy {

    private static final long DEFAULT_FARE = 1250;

    @Override
    public long calculateFare(final long fare, final Passenger passenger, final Subway subway) {
        final long distance = subway.calculateShortestDistance(passenger.getStart(), passenger.getEnd());

        long totalFare = DEFAULT_FARE;
        for (final DistancePolicy distancePolicy : DistancePolicy.values()) {
            totalFare += distancePolicy.calculateAdditionFare(distance);
        }
        return fare + totalFare;
    }
}