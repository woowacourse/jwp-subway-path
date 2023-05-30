package subway.domain.fare;

import subway.domain.route.Route;

public class DistanceAdditionalFarePolicy implements FarePolicy {

    @Override
    public Fare calculate(final Route route, final Integer age, final Fare fare) {
        int distance = route.findTotalDistance();
        Fare baseAdditionalFare = calculateAdditionalFare(distance, DistanceFareRate.BASE);
        Fare excessAdditionalFare = calculateAdditionalFare(distance, DistanceFareRate.EXCESS);
        Fare additionalFare = baseAdditionalFare.plus(excessAdditionalFare);
        return fare.plus(additionalFare);
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
