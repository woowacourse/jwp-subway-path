package subway.domain.fare;

import subway.domain.path.Path;

public class AgeDiscountFarePolicy implements FarePolicy {

    @Override
    public int calculate(final Path path, final Passenger passenger, final int fare) {
        final AgeGroup ageGroup = passenger.calulateAgeGroup();
        final int discountedFare = fare - ageGroup.getDiscountFare();
        return (int) (discountedFare - discountedFare * ageGroup.getDiscountRatio());
    }
}
