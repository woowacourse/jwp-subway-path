package subway.application.charge;

import org.springframework.stereotype.Component;

import static subway.application.charge.ChargeDistance.LONG;
import static subway.application.charge.ChargeDistance.SHORT;

@Component
public class DefaultChargePolicy implements ChargePolicy {

    private static final int DEFAULT_CHARGE = 1250;
    private static final int UNIT_CHARGE = 100;
    private static final int ENSURE_MINIMUM = 1;
    private static final int ADJUST = -1;

    @Override
    public int calculateFee(int distance) {
        if (distance > SHORT.getDistance()) {
            return calculateAdditionalFee(distance);
        }
        return DEFAULT_CHARGE;
    }

    private int calculateAdditionalFee(int distance) {
        if (distance <= LONG.getDistance()) {
            return DEFAULT_CHARGE + calculateDistanceFee(distance - SHORT.getDistance(), SHORT.getUnit());
        }
        return DEFAULT_CHARGE +
                calculateDistanceFee(LONG.getDistance() - SHORT.getDistance(), SHORT.getUnit())
                + calculateDistanceFee(distance - LONG.getDistance(), LONG.getUnit());
    }

    private int calculateDistanceFee(int distance, int unit) {
        return (((distance + ADJUST) / unit) + ENSURE_MINIMUM) * UNIT_CHARGE;
    }
}
