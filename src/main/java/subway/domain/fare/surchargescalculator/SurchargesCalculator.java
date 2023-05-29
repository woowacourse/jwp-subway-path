package subway.domain.fare.surchargescalculator;

import subway.domain.fare.Fare;

public interface SurchargesCalculator {
    Fare BASIC_FARE = new Fare(1_250);

    int FIRST_THRESHOLD = 10;

    int SECOND_THRESHOLD = 50;

    double FIRST_STANDARD = 5.0;

    double SECOND_STANDARD = 8.0;

    int ADDITIONAL_FEE = 100;

    boolean isAcceptable(final int distance);

    Fare calculate(final int distance);
}
