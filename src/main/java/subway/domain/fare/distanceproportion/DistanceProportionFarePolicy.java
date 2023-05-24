package subway.domain.fare.distanceproportion;

import subway.domain.fare.Fare;

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
        validate(lowerBoundDistance, maxBoundDistance, surchargeDistanceUnit);
        this.lowerBoundDistance = lowerBoundDistance;
        this.maxBoundDistance = maxBoundDistance;
        this.surchargeDistanceUnit = surchargeDistanceUnit;
    }

    private void validate(final int lowerBoundDistance, final int maxBoundDistance, final int surchargeDistanceUnit) {
        if (lowerBoundDistance >= maxBoundDistance) {
            throw new IllegalStateException("최소 범위는 최대 범위보다 작아야 합니다.");
        }

        if (surchargeDistanceUnit <= 0) {
            throw new IllegalStateException("할증 단위 범위는 0보다 커야 합니다.");
        }
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
