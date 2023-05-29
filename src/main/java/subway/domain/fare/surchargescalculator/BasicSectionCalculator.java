package subway.domain.fare.surchargescalculator;

import subway.domain.fare.Fare;

public class BasicSectionCalculator implements SurchargesCalculator {
    @Override
    public boolean isAcceptable(final int distance) {
        return distance <= FIRST_THRESHOLD;
    }

    @Override
    public Fare calculate(final int distance) {
        return BASIC_FARE;
    }
}
