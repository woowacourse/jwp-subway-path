package subway.domain.pay;

import subway.domain.Distance;
import subway.domain.Money;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Payment {
    GREATER_THAN_0_EQUALS_OR_LESS_THAN_10(
            DistancePrice.from(100_000, 0D),
            totalDistance -> 0 < totalDistance && totalDistance <= 10
    ),
    GREATER_THAN_10_EQUALS_OR_LESS_THAN_50(
            DistancePrice.from(5, 100D),
            totalDistance -> 10 < totalDistance && totalDistance <= 50
    ),
    GREATER_THAN_50(
            DistancePrice.from(8, 100D),
            totalDistance -> 50 < totalDistance && totalDistance <= 100_000
    );

    private static final Money DEFAULT_PRICE = Money.from(1250D);
    private static final Distance DEFAULT_DISTANCE = new Distance(10);

    private final DistancePrice distancePrice;
    private final Predicate<Integer> distanceRange;

    Payment(final DistancePrice distancePrice, final Predicate<Integer> distanceRange) {
        this.distancePrice = distancePrice;
        this.distanceRange = distanceRange;
    }

    public static Money calculate(final Distance totalDistance) {
        final Payment pay = findByDistance(totalDistance);

        final Distance additionalDistance = totalDistance.minus(DEFAULT_DISTANCE);
        final Money additionalPrice = pay.distancePrice.impose(additionalDistance);
        return DEFAULT_PRICE.plus(additionalPrice);
    }

    private static Payment findByDistance(final Distance totalDistance) {
        return Arrays.stream(Payment.values())
                .filter(payment -> payment.distanceRange.test(totalDistance.getValue()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("계산 할 수 없는 거리의 범위 입니다."));
    }
}
