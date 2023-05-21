package subway.domain.fare;

import subway.domain.Route;

public class DistanceAdditionalFarePolicy implements FarePolicy {

    @Override
    public Fare calculate(final Route route) {
        int distance = route.getDistance();
        Fare baseAdditionalFare = calculateAdditionalFare(distance, DistanceFareRate.BASE);
        Fare excessAdditionalFare = calculateAdditionalFare(distance, DistanceFareRate.EXCESS);
        return baseAdditionalFare.plus(excessAdditionalFare);
    }

    private Fare calculateAdditionalFare(final int distance, final DistanceFareRate distanceFareRate) {
        int additionalDistance = getAdditionalDistance(distance, distanceFareRate);
        if (additionalDistance <= 0) {
            return new Fare(0);
        }

        return new Fare(
                (int) Math.ceil(additionalDistance / (double) distanceFareRate.interval())
                        * distanceFareRate.farePerInterval());
    }

    private int getAdditionalDistance(final int distance, final DistanceFareRate distanceFareRate) {
        if (distanceFareRate == DistanceFareRate.BASE && distance > DistanceFareRate.EXCESS.standardDistance()) {
            return DistanceFareRate.EXCESS.standardDistance() - distanceFareRate.standardDistance();
        }
        return distance - distanceFareRate.standardDistance();
    }
}
