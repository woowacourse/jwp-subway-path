package subway.domain.payment;

import org.springframework.stereotype.Component;
import subway.domain.Lines;

@Component
public class DefaultPaymentPolicy implements PaymentPolicy {

    private static final int BASIC_FEE = 1250;
    private static final int BASIC_LIMIT_DISTANCE = 10;
    private static final int INCREASE_AMOUNT = 100;

    private static final int MID_DISTANCE_UNIT = 5;
    private static final int MID_LIMIT_DISTANCE = 40;

    private static final int LONG_DISTANCE_UNIT = 8;

    private static boolean isMidDistance(final int remainBasic) {
        return remainBasic <= MID_LIMIT_DISTANCE;
    }

    @Override
    public int calculateFee(final Lines lines) {
        if (lines.totalDistance() == 0) {
            return 0;
        }
        return calculateByDistance(lines.totalDistance());
    }

    private int calculateByDistance(final int totalDistance) {
        if (isBasicDistance(totalDistance)) {
            return BASIC_FEE;
        }
        return additionalFee(totalDistance - BASIC_LIMIT_DISTANCE);
    }

    private boolean isBasicDistance(final int totalDistance) {
        return totalDistance <= BASIC_LIMIT_DISTANCE;
    }

    private int additionalFee(final int remainBasic) {
        if (isMidDistance(remainBasic)) {
            return BASIC_FEE + calculateSurcharge(remainBasic, MID_DISTANCE_UNIT);
        }
        return longDistanceFee(remainBasic);
    }

    private int longDistanceFee(final int remainBasic) {
        final int remainMiddle = remainBasic - MID_LIMIT_DISTANCE;
        return BASIC_FEE
                + calculateSurcharge(MID_LIMIT_DISTANCE, MID_DISTANCE_UNIT)
                + calculateSurcharge(remainMiddle, LONG_DISTANCE_UNIT);
    }

    private int calculateSurcharge(final int remainBasic, final int unit) {
        return (((remainBasic - 1) / unit) + 1) * INCREASE_AMOUNT;
    }
}
