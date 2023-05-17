package subway.domain.fare;

import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;

public class AgeFareStrategy implements FareStrategy {

    private static final int BASE_DEDUCTION = 350;

    @Override
    public double calculateFare(final double fare, final Passenger passenger, final Subway subway) {
        final AgePolicy agePolicy = AgePolicy.search(passenger.getAge());
        final double fareAfterDeduction = Math.max(0, fare - BASE_DEDUCTION);
        return Math.max(0, fareAfterDeduction - agePolicy.calculateDiscountFare(fareAfterDeduction));
    }
}
