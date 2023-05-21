package subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class DistanceStrategy implements FareStrategy {

    public static final Fare BASIC_FARE = new Fare(1_250);
    public static final int BASE_THRESHOLD = 10;
    public static final int ADDITIONAL_THRESHOLD = 50;
    public static final double FIRST_STANDARD = 5.0;
    public static final double SECOND_STANDARD = 8.0;
    public static final int ADDITIONAL_FEE = 100;


    @Override
    public Fare calculate(final int distance) {
        validateDistance(distance);

        if (distance <= BASE_THRESHOLD) {
            return BASIC_FARE;
        }

        if (distance <= ADDITIONAL_THRESHOLD) {
            int surcharge = calculateSurcharge(distance, BASE_THRESHOLD, FIRST_STANDARD);
            return BASIC_FARE.sum(new Fare(surcharge));
        }

        int surcharge = calculateSurcharge(ADDITIONAL_THRESHOLD, BASE_THRESHOLD, FIRST_STANDARD)
                + calculateSurcharge(distance, ADDITIONAL_THRESHOLD, SECOND_STANDARD);
        return BASIC_FARE.sum(new Fare(surcharge));
    }

    private int calculateSurcharge(final int distance, final int threshold, final double standard) {
        return ADDITIONAL_FEE * (int) Math.ceil((distance - threshold) / standard);
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리가 0인 잘못된 경로입니다.");
        }
    }
}
