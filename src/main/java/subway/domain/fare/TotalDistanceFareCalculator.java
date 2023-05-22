package subway.domain.fare;

import java.util.List;

public class TotalDistanceFareCalculator {

    private static final Fare BASE_FARE = new Fare(1_250);

    private final List<DistanceProportionFarePolicy> distanceProportionFarePolicies;

    public TotalDistanceFareCalculator(final List<DistanceProportionFarePolicy> distanceProportionFarePolicies) {
        this.distanceProportionFarePolicies = distanceProportionFarePolicies;
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
