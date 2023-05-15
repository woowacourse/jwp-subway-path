package subway.domain;

import subway.application.exception.ExceptionMessages;
import subway.application.exception.InvalidDistanceException;

public class EmptyDistance extends Distance {
    public EmptyDistance() {
        super(0);
    }

    @Override
    public Distance subtract(Distance distance) {
        throw new InvalidDistanceException(ExceptionMessages.INVALID_DISTANCE);
    }
}
