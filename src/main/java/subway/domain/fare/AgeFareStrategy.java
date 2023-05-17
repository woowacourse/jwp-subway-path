package subway.domain.fare;

import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;

public class AgeFareStrategy implements FareStrategy {

    @Override
    public double calculateFare(final double fare, final Passenger passenger, final Subway subway) {
        final AgePolicy agePolicy = AgePolicy.search(passenger.getAge());
        return agePolicy.calculateDiscountFare(fare);
    }
}
