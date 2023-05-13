package subway.domain.payment;

import org.springframework.stereotype.Component;
import subway.domain.LinkedRoute;

@Component
public class DefaultPaymentPolicy implements PaymentPolicy {

    private static final int BASIC_PAYMENT = 1250;
    private static final int BASIC_LIMIT_DISTANCE = 10;
    private static final int INCREASE_AMOUNT = 100;

    private static final int MIDDLE_LENGTH_UNIT = 5;
    private static final int MIDDLE_LIMIT_DISTANCE = 40;

    private static final int LONG_DISTANCE_SLICE = 8;

    @Override
    public int calculatePayment(final LinkedRoute route) {
        if (route.isEmpty() || route.totalDistance() == 0) {
            return 0;
        }

        final int totalDistance = route.totalDistance();
        if (totalDistance <= BASIC_LIMIT_DISTANCE) {
            return BASIC_PAYMENT;
        }

        final int remainBasic = totalDistance - BASIC_LIMIT_DISTANCE;
        if (remainBasic <= MIDDLE_LIMIT_DISTANCE) {
            return BASIC_PAYMENT + calculateSurcharge(remainBasic, MIDDLE_LENGTH_UNIT);
        }

        final int remainMiddle = remainBasic - MIDDLE_LIMIT_DISTANCE;

        return BASIC_PAYMENT
                + calculateSurcharge(MIDDLE_LIMIT_DISTANCE, MIDDLE_LENGTH_UNIT)
                + calculateSurcharge(remainMiddle, LONG_DISTANCE_SLICE);
    }

    private static int calculateSurcharge(final int remainBasic, final int unit) {
        return (((remainBasic - 1) / unit) + 1) * INCREASE_AMOUNT;
    }
}
