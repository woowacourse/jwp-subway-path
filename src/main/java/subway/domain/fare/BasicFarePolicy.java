package subway.domain.fare;

import subway.domain.path.Path;

public class BasicFarePolicy implements FarePolicy {

    private static final int BASIC_FARE = 1250;

    @Override
    public int calculateFare(Path path) {
        return BASIC_FARE;
    }
}
