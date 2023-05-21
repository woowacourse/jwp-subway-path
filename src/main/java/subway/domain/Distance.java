package subway.domain;

import java.util.Objects;

public class Distance {

    private static Long sequence = 1L;
    private Long id;
    private Integer value = 0;

    public Distance(Integer value) {
        this.id = sequence++;
        this.value = value;
    }

    private Distance() {
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
