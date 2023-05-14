package subway.application.feepolicy;

import org.springframework.stereotype.Component;

@Component
public class DefaultFeePolicy implements FeePolicy {
    private static final int BASIC_DISTANCE = 10;
    private static final int ADDITIONAL_DISTANCE = 50;
    private static final int ADDITIONAL_FEE = 100;

    @Override
    public int calculateFee(final int distance) {

        int fee = 1250;
        int distanceBeforeFifty = 0;
        int distanceAfterFifty = 0;

        distanceBeforeFifty = distance - BASIC_DISTANCE;
        distanceAfterFifty = distance - ADDITIONAL_DISTANCE;
        if (distance > ADDITIONAL_DISTANCE) {
            distanceBeforeFifty = ADDITIONAL_DISTANCE - BASIC_DISTANCE;
        }

        if (distanceBeforeFifty > 0) {
            fee += Math.ceil(distanceBeforeFifty / 5d) * ADDITIONAL_FEE;
        }
        if (distanceAfterFifty > 0) {
            fee += Math.ceil(distanceAfterFifty / 8d) * ADDITIONAL_FEE;
        }

        return fee;
    }
}
