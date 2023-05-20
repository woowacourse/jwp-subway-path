package subway.domain;

import java.util.Objects;
import subway.controller.exception.InvalidStationException;

public class Station {
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 10;

    private final String name;

    public Station(final String name) {
        validate(name);
        this.name = name;
    }

    private void validate(final String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new InvalidStationException(String.format("역 이름은 %d~%d자 사이여야 합니다", MIN_NAME_LENGTH, MAX_NAME_LENGTH));
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Station)) {
            return false;
        }
        final Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
