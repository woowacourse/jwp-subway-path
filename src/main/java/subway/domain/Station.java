package subway.domain;

import java.util.Objects;
import subway.exception.ErrorMessage;
import subway.exception.InvalidException;

public class Station {
    private final Long id;
    private final String name;

    public Station(final Long id, final String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public static Station createWithoutId(final String name) {
        return new Station(null, name);
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new InvalidException(ErrorMessage.INVALID_BLANK_NAME);
        }
    }

    public Long getId() {
        return id;
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
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
