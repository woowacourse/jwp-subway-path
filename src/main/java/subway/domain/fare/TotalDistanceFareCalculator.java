package subway.domain.fare;

import java.util.List;

public class TotalDistanceFareCalculator {
    private static final DistanceProportionFarePolicy FIRST_DISTANCE_RANGE
            = new DistanceProportionFarePolicy(10, 50, 5);
    private static final DistanceProportionFarePolicy SECOND_DISTANCE_RANGE
            = new DistanceProportionFarePolicy(50, 1_000_000, 8);
    private static final Fare BASE_FARE = new Fare(1_250);

    private final List<DistanceProportionFarePolicy> distanceProportionFarePolicies;

    public TotalDistanceFareCalculator() {
        this.distanceProportionFarePolicies = List.of(FIRST_DISTANCE_RANGE, SECOND_DISTANCE_RANGE);
    }

    public Fare calculateFareByDistance(final int distance) {
        Fare totalFare = BASE_FARE;
        for (final DistanceProportionFarePolicy distanceProportionFarePolicy : distanceProportionFarePolicies) {
            final Fare additionalFare = distanceProportionFarePolicy.calculate(distance);
            totalFare = totalFare.add(additionalFare);
        }

        return totalFare;
    }
}
