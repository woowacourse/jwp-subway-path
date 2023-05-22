package subway.application.costpolicy;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import subway.domain.vo.Distance;
import subway.exception.BusinessException;

public enum DistanceCost {

    DEFAULT((cost, distance) -> cost,
        distance -> distance.isMoreThan(Distance.from(1L)) && distance.isLessThan(Distance.from(10L))),
    FIRST_ADDITION(calculateFirstAdditionalCost(),
        distance -> distance.isExceedThan(Distance.from(10L)) && distance.isLessThan(Distance.from(50L))),
    SECOND_ADDITION(calculateSecondAdditionalCost(),
        distance -> distance.isExceedThan(Distance.from(50L)));

    private final BiFunction<Long, Distance, Long> additionalCost;

    private final Predicate<Distance> distanceRange;

    DistanceCost(final BiFunction<Long, Distance, Long> additionalCost, final Predicate<Distance> distanceRange) {
        this.additionalCost = additionalCost;
        this.distanceRange = distanceRange;
    }

    private static BiFunction<Long, Distance, Long> calculateFirstAdditionalCost() {
        return (cost, distance) -> {
            final long additionalDistance = Math.min(distance.getValue(), 50) - 10L;
            return cost + (long) ((Math.ceil((double) additionalDistance / 5)) * 100);
        };
    }

    private static BiFunction<Long, Distance, Long> calculateSecondAdditionalCost() {
        return (cost, distance) -> {
            final Long firstCost = FIRST_ADDITION.additionalCost.apply(cost, distance);
            final long additionalDistance = distance.getValue() - 50;
            return firstCost + (long) ((Math.ceil((double) additionalDistance / 8)) * 100);

        };
    }

    public static long calculateAdditionalCost(final long cost, final Distance distance) {
        final DistanceCost distanceCost = findByDistance(distance);
        return distanceCost.additionalCost.apply(cost, distance);
    }

    private static DistanceCost findByDistance(final Distance distance) {
        return Arrays.stream(values())
            .filter(distanceCost -> distanceCost.distanceRange.test(distance))
            .findAny()
            .orElseThrow(() -> new BusinessException("적절하지 않은 거리입니다."));
    }
}
