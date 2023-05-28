package subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class DistanceFareStrategy implements FareStrategy {

    private static final int BASE_FARE = 1250;
    private static final int STANDARD_DISTANCE_10KM = 10;
    private static final int STANDARD_DISTANCE_50KM = 50;
    private static final double SECTION_DISTANCE_5KM = 5.0;
    private static final double SECTION_DISTANCE_8KM = 8.0;

    @Override
    public int calculateFare(final int distance) {
        int surcharge = 0;
        if (distance > STANDARD_DISTANCE_10KM) {
            surcharge += Math.min(calculateFareByDistance(distance, STANDARD_DISTANCE_10KM, SECTION_DISTANCE_5KM), 800);
        }
        if (distance > STANDARD_DISTANCE_50KM) {
            surcharge += calculateFareByDistance(distance, STANDARD_DISTANCE_50KM, SECTION_DISTANCE_8KM);
        }
        return BASE_FARE + surcharge;
    }

    private int calculateFareByDistance(final int distance, final int standardDistance, final double sectionDistance) {
        return (int) ((Math.floor((distance - standardDistance - 1) / sectionDistance) + 1) * 100);
    }
}
