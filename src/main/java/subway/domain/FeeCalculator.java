package subway.domain;

import subway.domain.feePolicy.AgePolicy;
import subway.domain.feePolicy.DistancePolicy;
import subway.domain.feePolicy.LinePolicy;

public class FeeCalculator {

    private static final int DEFAULT_FEE = 1250;

    private final DistancePolicy distancePolicy;
    private final LinePolicy linePolicy;
    private final AgePolicy agePolicy;

    public FeeCalculator(final DistancePolicy distancePolicy, final AgePolicy agePolicy) {
        this.distancePolicy = distancePolicy;
        this.linePolicy = new LinePolicy();
        this.agePolicy = agePolicy;
    }

    public int calculate(final Path shortestPath) {
        int totalFee = DEFAULT_FEE;

        totalFee += distancePolicy.calculate(shortestPath.getDistance());
        totalFee += linePolicy.calculateExtraFee(shortestPath.getPassLines());
        totalFee = agePolicy.discount(totalFee);

        return totalFee;
    }
}
