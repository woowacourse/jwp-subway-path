package subway.domain.feePolicy;

import subway.domain.Distance;
import java.util.Arrays;

public enum DistancePolicy {

    DEFAULT(0, 10, 0, 0, 0),
    _11_To_50_KM(11, 50, 10, 5, 100),
    OVER_51_KM(51, Double.MAX_VALUE, 50, 8, 900);

    private static final int EXTRA_FEE = 100;
    private static final int NO_EXTRA_FEE = 0;

    private final Distance minDistance;
    private final Distance maxDistance;
    private final Distance criteriaDistance;
    private final int unitDistance;
    private final int defaultExtraFee;

    DistancePolicy(final double minDistance, final double maxDistance, final double criteriaDistance, final int unitDistance, final int defaultExtraFee) {
        this.minDistance = new Distance(minDistance);
        this.maxDistance = new Distance(maxDistance);
        this.criteriaDistance = new Distance(criteriaDistance);
        this.unitDistance = unitDistance;
        this.defaultExtraFee = defaultExtraFee;
    }

    public static DistancePolicy from(final Distance distance) {
        return Arrays.stream(DistancePolicy.values())
                .filter(policy -> distance.isInclude(policy.minDistance, policy.maxDistance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("입력한 distance로 적절한 DistancePolicy를 찾지 못했습니다. 입력값:" + distance));
    }

    public int calculate(final Distance distance) {
        if (this == DEFAULT) {
            return NO_EXTRA_FEE;
        }

        int extraDistance = (int) Math.ceil(distance.subtract(criteriaDistance).getValue());
        if (extraDistance % unitDistance == 0) {
            extraDistance--;
        }
        return defaultExtraFee + ((extraDistance / unitDistance) * EXTRA_FEE);
    }
}
