package subway.path.domain.payment;

import org.springframework.stereotype.Component;
import subway.line.exception.line.LineException;
import subway.path.domain.Path;

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
    public int calculateFee(final Path path) {
        validateLinesDistance(path);
        return calculateByDistance(path.totalDistance());
    }

    private void validateLinesDistance(final Path path) {
        if (path.totalDistance() == 0) {
            throw new LineException("경로의 거리는 0일 수 없습니다");
        }
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

    private int additionalFee(final int remainBasicDistance) {
        if (isMidDistance(remainBasicDistance)) {
            return BASIC_FEE + calculateSurcharge(remainBasicDistance, MID_DISTANCE_UNIT);
        }
        return longDistanceFee(remainBasicDistance);
    }

    private int longDistanceFee(final int remainMidDistance) {
        final int remainMiddle = remainMidDistance - MID_LIMIT_DISTANCE;
        return BASIC_FEE
                + calculateSurcharge(MID_LIMIT_DISTANCE, MID_DISTANCE_UNIT)
                + calculateSurcharge(remainMiddle, LONG_DISTANCE_UNIT);
    }

    private int calculateSurcharge(final int distance, final int unit) {
        return (((distance - 1) / unit) + 1) * INCREASE_AMOUNT;
    }
}
