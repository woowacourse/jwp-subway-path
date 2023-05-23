package subway.domain.fare;

import java.util.Objects;
import subway.exception.InvalidAgeException;

public class Age {

    private final int value;

    public Age(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 1) {
            throw new InvalidAgeException("나이는 1살 이상 부터 가능합니다.");
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Age age = (Age) o;
        return getValue() == age.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
