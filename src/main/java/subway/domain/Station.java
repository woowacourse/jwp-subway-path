package subway.domain;

import java.util.Objects;
import subway.exception.ErrorCode;
import subway.exception.InvalidException;

public class Station {
    private final String name;

    public Station(final String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new InvalidException(ErrorCode.INVALID_BLANK_NAME);
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
