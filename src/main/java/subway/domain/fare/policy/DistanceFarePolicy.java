package subway.domain.fare.policy;


import org.springframework.stereotype.Component;
import subway.domain.fare.FareInformation;

@Component
public class DistanceFarePolicy implements FarePolicy {

    private static final int DEFAULT_FEE_BOUNDARY = 10;
    private static final int FIRST_ADDITIONAL_FEE_BOUNDARY = 50;
    private static final int UNIT_OF_ADDITIONAL_FEE = 100;
    private static final double UNIT_OF_FIRST_ADDITIONAL_FEE_DISTANCE = 5d;
    private static final double UNIT_OF_SECOND_ADDITIONAL_FEE_DISTANCE = 8d;

    @Override
    public int calculate(final FareInformation fareInformation) {
        int distance = fareInformation.getDistance();
        validateDistance(distance);
        if (distance <= DEFAULT_FEE_BOUNDARY) {
            return 0;
        }

        return getAdditionalFee(distance);
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 0이하일 수 없습니다.");
        }
    }

    private int getAdditionalFee(final int distance) {
        final int additionalDistance = Math.min(distance, FIRST_ADDITIONAL_FEE_BOUNDARY) - DEFAULT_FEE_BOUNDARY;
        int additionalFee = (int) (Math.ceil(additionalDistance / UNIT_OF_FIRST_ADDITIONAL_FEE_DISTANCE) * UNIT_OF_ADDITIONAL_FEE);

        if (distance >= FIRST_ADDITIONAL_FEE_BOUNDARY) {
            final int remainingDistance = distance - FIRST_ADDITIONAL_FEE_BOUNDARY;
            additionalFee += (int) (Math.ceil(remainingDistance / UNIT_OF_SECOND_ADDITIONAL_FEE_DISTANCE) * UNIT_OF_ADDITIONAL_FEE);
        }

        return additionalFee;
    }
}
