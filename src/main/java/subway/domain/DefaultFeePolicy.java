package subway.domain;


import org.springframework.stereotype.Component;

//TODO: 상수명 및 클래스 명 변경
@Component
public class DefaultFeePolicy implements FeePolicy {

    private static final int MAXIMUM_DISTANCE_TO_DEFAULT_FEE = 10;
    private static final int DEFAULT_FEE = 1250;
    private static final int UNIT_OF_ADDITIONAL_FEE = 100;

    @Override
    public int calculate(int distance) {
        validateDistance(distance);
        if (distance <= MAXIMUM_DISTANCE_TO_DEFAULT_FEE) {
            return DEFAULT_FEE;
        }

        return DEFAULT_FEE + getAdditionalFee(distance);
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 0이하일 수 없습니다.");
        }
    }

    private int getAdditionalFee(int distance) {
        int additionalDistance = Math.min(distance, 50) - MAXIMUM_DISTANCE_TO_DEFAULT_FEE;
        int additionalFee = (int) (Math.ceil(additionalDistance / 5d) * UNIT_OF_ADDITIONAL_FEE);

        if (distance >= 50) {
            int remainingDistance = distance - 50;
            additionalFee += (int) (Math.ceil(remainingDistance / 8d) * UNIT_OF_ADDITIONAL_FEE);
        }

        return additionalFee;
    }
}
