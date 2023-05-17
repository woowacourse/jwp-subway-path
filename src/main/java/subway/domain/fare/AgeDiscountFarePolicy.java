package subway.domain.fare;

import subway.domain.path.PathFindResult;

public class AgeDiscountFarePolicy implements FarePolicy {

    @Override
    public int calculate(final PathFindResult result, final Passenger passenger, final int fare) {
        return passenger.calculateFare(fare);
    }
}
