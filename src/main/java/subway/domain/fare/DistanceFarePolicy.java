package subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class DistanceFarePolicy implements FarePolicy {

    private static final int BASIC_FARE = 1250;
    public static final int FIRST_DISTANCE_THRESHOLD = 10;
    public static final int SECOND_DISTANCE_THRESHOLD = 50;
    public static final int FIRST_DISTANCE_THRESHOLD_FARE_INCREMENT = 5;
    public static final int SECOND_DISTANCE_THRESHOLD_FARE_INCREMENT = 8;
    public static final int ADDITIONAL_FARE = 100;

    @Override
    public int calculateFare(int distance) {
        if (distance > 50) {
            return calculateFare(50) +
                    (int) Math.ceil((double) (distance - SECOND_DISTANCE_THRESHOLD) / SECOND_DISTANCE_THRESHOLD_FARE_INCREMENT) * ADDITIONAL_FARE;
        }
        if (distance > 10) {
            return BASIC_FARE +
                    (int) Math.ceil((double) (distance - FIRST_DISTANCE_THRESHOLD) / FIRST_DISTANCE_THRESHOLD_FARE_INCREMENT) * ADDITIONAL_FARE;
        }
        return BASIC_FARE;
    }
}
