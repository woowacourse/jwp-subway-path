package subway.domain.payment;

import org.springframework.stereotype.Component;
import subway.domain.Lines;

@Component
public class DefaultPaymentPolicy implements PaymentPolicy {

    private static final int BASIC_FEE = 1250;
    private static final int BASIC_LIMIT_DISTANCE = 10;
    private static final int INCREASE_AMOUNT = 100;

    private static final int MIDDLE_LENGTH_UNIT = 5;
    private static final int MIDDLE_LIMIT_DISTANCE = 40;

    private static final int LONG_DISTANCE_SLICE = 8;

    @Override
    public int calculateFee(final Lines lines) {
        if (lines.isEmpty() || lines.totalDistance() == 0) {
            return 0;
        }

        final int totalDistance = lines.totalDistance();
        if (totalDistance <= BASIC_LIMIT_DISTANCE) {
            return BASIC_FEE;
        }

        final int remainBasic = totalDistance - BASIC_LIMIT_DISTANCE;
        if (remainBasic <= MIDDLE_LIMIT_DISTANCE) {
            return BASIC_FEE + calculateSurcharge(remainBasic, MIDDLE_LENGTH_UNIT);
        }

        final int remainMiddle = remainBasic - MIDDLE_LIMIT_DISTANCE;

        return BASIC_FEE
                + calculateSurcharge(MIDDLE_LIMIT_DISTANCE, MIDDLE_LENGTH_UNIT)
                + calculateSurcharge(remainMiddle, LONG_DISTANCE_SLICE);
    }

    private static int calculateSurcharge(final int remainBasic, final int unit) {
        return (((remainBasic - 1) / unit) + 1) * INCREASE_AMOUNT;
    }
}
