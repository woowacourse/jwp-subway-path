package subway.domain.fare;

import java.util.List;
import subway.domain.subwaymap.LineStation;

public class DistanceFarePolicy implements FarePolicy {

    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int FIRST_BOUND = 50;
    private static final int FIRST_BOUND_EXCEED_UNIT = 8;
    private static final int SECOND_BOUND = 10;
    private static final int SECOND_BOUND_EXCEED_UNIT = 5;

    @Override
    public int calculate(int fare, final List<LineStation> lineStations, int distance, final int age) {
        validate(fare, distance, age);

        fare = BASIC_FARE;

        if (distance > FIRST_BOUND) {
            fare += getOveredFare(distance - FIRST_BOUND, FIRST_BOUND_EXCEED_UNIT);
            distance = FIRST_BOUND;
        }

        if (distance > SECOND_BOUND) {
            fare += getOveredFare(distance - SECOND_BOUND, SECOND_BOUND_EXCEED_UNIT);
        }
        return fare;
    }

    private int getOveredFare(final int distance, final int bound) {
        int exceedCount = distance / bound;
        if (distance % bound != 0) {
            exceedCount++;
        }
        return exceedCount * ADDITIONAL_FARE;
    }
}
