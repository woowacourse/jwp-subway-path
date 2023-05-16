package subway.domain.billing;

import subway.exception.InvalidDistanceException;

public class DistanceBillingPolicy implements BillingPolicy {

    private static final int BASELINE_DISTANCE = 10;
    private static final int BASELINE_FEE = 1250;
    private static final int EXTRA_CHARGE_DISTANCE = 5;
    private static final int EXTRA_CHARGE_FEE = 100;

    @Override
    public int calculateFare(final int distance) {
        if (distance < 0) {
            throw new InvalidDistanceException("거리가 0보다 작은 경우 요금을 계산할 수 없습니다.");
        }
        if (distance <= BASELINE_DISTANCE) {
            return BASELINE_FEE * (int) Math.ceil((double) distance / BASELINE_DISTANCE);
        }
        final int overDistance = distance - BASELINE_DISTANCE;
        return BASELINE_FEE + calculateOverFare(overDistance);
    }

    private int calculateOverFare(final int distance) {
        return (int) ((Math.ceil((distance - 1) / EXTRA_CHARGE_DISTANCE) + 1) * EXTRA_CHARGE_FEE);
    }
}
