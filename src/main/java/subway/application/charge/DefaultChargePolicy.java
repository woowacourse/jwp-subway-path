package subway.application.charge;

import org.springframework.stereotype.Component;

@Component
public class DefaultChargePolicy implements ChargePolicy {

    private static final int DEFAULT_CHARGE = 1250;
    private static final int SHORT_DISTANCE = 10;
    private static final int LONG_DISTANCE = 50;
    private static final int SHORT_DISTANCE_UNIT = 5;
    private static final int LONG_DISTANCE_UNIT = 8;

    @Override
    public int calculateFee(int distance) {
        if (distance > SHORT_DISTANCE) {
            return calculateAdditionalFee(distance);
        }
        return DEFAULT_CHARGE;
    }

    private int calculateAdditionalFee(int distance) {
        if (distance <= LONG_DISTANCE) {
            return DEFAULT_CHARGE + calculateDistanceFee(distance - SHORT_DISTANCE, SHORT_DISTANCE_UNIT);
        }
        return DEFAULT_CHARGE +
                calculateDistanceFee(LONG_DISTANCE - SHORT_DISTANCE, SHORT_DISTANCE_UNIT)
                + calculateDistanceFee(distance - LONG_DISTANCE, LONG_DISTANCE_UNIT);
    }

    private int calculateDistanceFee(int distance, int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * 100);
    }
}
