package subway.domain.farepolicy;

import subway.domain.path.SubwayPath;

import java.util.List;

public class DefaultFarePolicy implements FarePolicy {

    public static final int DEFAULT_FARE = 1250;

    private final List<DistanceOverFarePolicy> distanceOverFarePolicies;

    public DefaultFarePolicy(final List<DistanceOverFarePolicy> distanceOverFarePolicies) {
        this.distanceOverFarePolicies = distanceOverFarePolicies;
    }

    @Override
    public Fare calculate(final SubwayPath subwayPath) {
        Fare totalFare = new Fare(DEFAULT_FARE);
        for (final DistanceOverFarePolicy distanceOverFarePolicy : distanceOverFarePolicies) {
            totalFare = totalFare.add(distanceOverFarePolicy.calculateOverFare(subwayPath));
        }
        return totalFare;

    }
}
