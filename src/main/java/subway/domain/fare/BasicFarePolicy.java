package subway.domain.fare;

import subway.domain.line.Line;

import java.util.Set;

public class BasicFarePolicy implements FarePolicy {

    private static final int BASIC_FARE = 1250;

    @Override
    public int calculateFare(int distance, Set<Line> linesToUse) {
        return BASIC_FARE;
    }
}
