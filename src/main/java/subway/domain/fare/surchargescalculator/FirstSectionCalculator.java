package subway.domain.fare.surchargescalculator;

import subway.domain.fare.Fare;

public class FirstSectionCalculator implements SurchargesCalculator {
    @Override
    public boolean isAcceptable(final int distance) {
        return (distance > FIRST_THRESHOLD) && (distance <= SECOND_THRESHOLD);
    }

    @Override
    public Fare calculate(final int distance) {
        int surcharge = ADDITIONAL_FEE * (int) Math.ceil((distance - FIRST_THRESHOLD) / FIRST_STANDARD);
        return BASIC_FARE.sum(new Fare(surcharge));
    }
}
