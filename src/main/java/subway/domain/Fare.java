package subway.domain;

import java.util.Objects;

public class Fare {
    final int value;

    public Fare(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("이용 요금은 양수여야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public Fare plus(Fare fare) {
        return new Fare(value + fare.value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value +
                '}';
    }
}
