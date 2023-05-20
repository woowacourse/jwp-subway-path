package subway.application.feecalculator;

import org.springframework.stereotype.Component;

@Component
public class DefaultFeeCalculator implements FeeCalculator {
    private static final int DEFAULT_FEE = 1250;
    private static final int ADDITIONAL_FEE = 100;
    private static final int BASIC_DISTANCE = 10;
    private static final int ADDITIONAL_DISTANCE = 50;
    private static final double UNIT_DISTANCE = 5d;
    private static final double LONGER_UNIT_DISTANCE = 8d;

    @Override
    public int calculateFee(final int distance) {

        if (distance <= BASIC_DISTANCE) {
            return DEFAULT_FEE;
        }
        return DEFAULT_FEE + calculateAdditionalFee(distance);
    }

    private int calculateAdditionalFee(final int distance) {
        if (distance <= ADDITIONAL_DISTANCE) {
            return calculateMathCeil(distance, BASIC_DISTANCE, UNIT_DISTANCE) * ADDITIONAL_FEE;
        }
        return calculateAdditionalFee(ADDITIONAL_DISTANCE) +
                calculateMathCeil(distance, ADDITIONAL_DISTANCE, LONGER_UNIT_DISTANCE) * ADDITIONAL_FEE;
    }

    private int calculateMathCeil(final int distance, final int subtractDistance, final double unitDistance) {
        return (int) (Math.ceil((distance - subtractDistance) / unitDistance));
    }
}
