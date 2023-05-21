package subway.domain.passenger;

import java.util.Objects;
import subway.exception.InvalidAgeException;

public final class Age {

    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 150;

    private final int value;

    public Age(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (MINIMUM > value || value > MAXIMUM) {
            throw new InvalidAgeException("나이는 0 ~ 150 사이의 값이어야 합니다.");
        }
    }

    public boolean isYoungerThanOrEqualTo(final Age target) {
        return target.getValue() >= value;
    }

    public boolean isOlderThanOrEqualTo(final Age target) {
        return target.getValue() <= value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Age age = (Age) o;
        return value == age.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
