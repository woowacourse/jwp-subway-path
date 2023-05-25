package subway.line.domain.section.domain;

import subway.line.domain.section.domain.exception.InvalidDistanceException;

public class EmptyDistance extends Distance {
    public EmptyDistance() {
        super(0);
    }

    @Override
    public Distance subtract(Distance distance) {
        throw new InvalidDistanceException();
    }

    @Override
    public boolean equals(Object o) {
        return getClass() == o.getClass();
    }
}
