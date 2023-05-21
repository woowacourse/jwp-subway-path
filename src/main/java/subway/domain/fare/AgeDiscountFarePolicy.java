package subway.domain.fare;

import subway.domain.path.Path;

public class AgeDiscountFarePolicy implements FarePolicy {

    @Override
    public int calculate(final Path path, final Passenger passenger, final int fare) {
        return passenger.calculateFare(fare);
    }
}
