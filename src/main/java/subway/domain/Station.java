package subway.domain;

import java.util.Objects;
import subway.exception.ErrorCode;
import subway.exception.InvalidException;

public class Station {
    private final Long id;
    private final String name;

    public Station(Long id, String name) {
        validateId(id);
        validateName(name);
        this.id = id;
        this.name = name;
    }

    private void validateId(Long id) {
        if (id <= 0) {
            throw new InvalidException(ErrorCode.INVALID_NOT_POSITIVE_ID);
        }
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new InvalidException(ErrorCode.INVALID_BLANK_NAME);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(id, station.id) && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
