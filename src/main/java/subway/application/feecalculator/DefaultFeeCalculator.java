package subway.application.feecalculator;

import org.springframework.stereotype.Component;

@Component
public class DefaultFeeCalculator implements FeeCalculator {
    private static final int DEFAULT_FEE = 1250;
    private static final int BASIC_DISTANCE = 10;
    private static final int ADDITIONAL_DISTANCE = 50;
    private static final int ADDITIONAL_FEE = 100;
    private static final double UNIT_DISTANCE = 5d;
    private static final double LONGER_UNIT_DISTANCE = 8d;

    @Override
    public int calculateFee(final int distance) {

        int fee = DEFAULT_FEE;
        int distanceBeforeFifty = 0;
        int distanceAfterFifty = 0;

        distanceBeforeFifty = distance - BASIC_DISTANCE;
        distanceAfterFifty = distance - ADDITIONAL_DISTANCE;
        if (distance > ADDITIONAL_DISTANCE) {
            distanceBeforeFifty = ADDITIONAL_DISTANCE - BASIC_DISTANCE;
        }

        if (distanceBeforeFifty > 0) {
            fee += Math.ceil(distanceBeforeFifty / UNIT_DISTANCE) * ADDITIONAL_FEE;
        }
        if (distanceAfterFifty > 0) {
            fee += Math.ceil(distanceAfterFifty / LONGER_UNIT_DISTANCE) * ADDITIONAL_FEE;
        }

        return fee;
    }
}
