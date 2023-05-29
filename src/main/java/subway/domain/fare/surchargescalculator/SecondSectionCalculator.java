package subway.domain.fare.surchargescalculator;

import subway.domain.fare.Fare;

public class SecondSectionCalculator implements SurchargesCalculator {
    @Override
    public boolean isAcceptable(final int distance) {
        return distance > SECOND_THRESHOLD;
    }

    @Override
    public Fare calculate(final int distance) {
        int surcharge = ADDITIONAL_FEE * (int) Math.ceil((SECOND_THRESHOLD - FIRST_THRESHOLD) / FIRST_STANDARD)
                + ADDITIONAL_FEE * (int) Math.ceil((distance - SECOND_THRESHOLD) / SECOND_STANDARD);
        return BASIC_FARE.sum(new Fare(surcharge));
    }
}
