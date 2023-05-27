package subway.domain.fee;

import org.springframework.stereotype.Component;
import subway.domain.Distance;

@Component
public class NormalFeeCalculator implements FeeCalculator {

    private static final Fee BASIC_FEE = new Fee(1250);
    private static final Fee EXTRA_FEE = new Fee(100);
    private static final Distance FIRST_LEVEL_START_DISTANCE = new Distance(10);
    private static final Distance FIRST_LEVEL_DISTANCE_FOR_EXTRA_FEE = new Distance(5);
    private static final Distance SECOND_LEVEL_START_DISTANCE = new Distance(50);
    private static final Distance SECOND_LEVEL_DISTANCE_FOR_EXTRA_FEE = new Distance(8);

    @Override
    public Fee calculate(final Distance distance) {
        if (distance.isLessThanOrEqualTo(FIRST_LEVEL_START_DISTANCE)) {
            return BASIC_FEE;
        }
        final Distance extraDistance = distance.subtract(FIRST_LEVEL_START_DISTANCE);
        return BASIC_FEE.add(calculateExtraFee(extraDistance));
    }

    private Fee calculateExtraFee(final Distance extraDistance) {
        final Distance extraDistanceForFirstLevel = SECOND_LEVEL_START_DISTANCE.subtract(FIRST_LEVEL_START_DISTANCE);
        if (extraDistance.isLessThanOrEqualTo(extraDistanceForFirstLevel)) {
            return calculateFirstLevelExtraFee(extraDistance);
        }
        final Distance extraDistanceForSecondLevel = extraDistance.subtract(extraDistanceForFirstLevel);
        final Fee extraFee = calculateFirstLevelExtraFee(extraDistanceForFirstLevel);
        return extraFee.add(calculateSecondLevelExtraFee(extraDistanceForSecondLevel));
    }

    private Fee calculateFirstLevelExtraFee(final Distance extraDistanceForFirstLevel) {
        Fee extraFee = Fee.getInitialFee();
        Distance extraDistance = extraDistanceForFirstLevel;
        while (extraDistance.isGreaterThan(FIRST_LEVEL_DISTANCE_FOR_EXTRA_FEE)) {
            extraFee = extraFee.add(EXTRA_FEE);
            extraDistance = extraDistance.subtract(FIRST_LEVEL_DISTANCE_FOR_EXTRA_FEE);
        }
        return extraFee.add(EXTRA_FEE);
    }

    private Fee calculateSecondLevelExtraFee(final Distance extraDistanceForSecondLevel) {
        Fee extraFee = Fee.getInitialFee();
        Distance extraDistance = extraDistanceForSecondLevel;
        while (extraDistance.isGreaterThan(SECOND_LEVEL_DISTANCE_FOR_EXTRA_FEE)) {
            extraFee = extraFee.add(EXTRA_FEE);
            extraDistance = extraDistance.subtract(SECOND_LEVEL_DISTANCE_FOR_EXTRA_FEE);
        }
        return extraFee.add(EXTRA_FEE);
    }
}
