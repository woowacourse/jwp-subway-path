package subway.domain.fare;

import subway.domain.route.Route;

public class BaseFarePolicy implements FarePolicy {

    private static final int BASE_FARE = 1250;

    @Override
    public Fare calculate(final Route route) {
        return new Fare(BASE_FARE);
    }
}
