package subway.domain.fare;

public class DistanceProportionFarePolicy {
    private static final int SURCHARGE_FARE = 100;

    private final int lowerBoundDistance;
    private final int maxBoundDistance;
    private final int surchargeDistanceUnit;

    public DistanceProportionFarePolicy(
            final int lowerBoundDistance,
            final int maxBoundDistance,
            final int surchargeDistanceUnit
    ) {
        this.lowerBoundDistance = lowerBoundDistance;
        this.maxBoundDistance = maxBoundDistance;
        this.surchargeDistanceUnit = surchargeDistanceUnit;
    }


    public Fare calculate(final int distance) {
        if (distance <= lowerBoundDistance) {
            return new Fare(0);
        }

        if (distance > maxBoundDistance) {
            return calculateExtraFare(maxBoundDistance);
        }

        return calculateExtraFare(distance);
    }

    private Fare calculateExtraFare(final int distance) {
        final int extraDistance = distance - lowerBoundDistance;
        final int extraFare = (int) (Math.ceil((extraDistance - 1) / surchargeDistanceUnit) + 1) * SURCHARGE_FARE;

        return new Fare(extraFare);
    }
}
