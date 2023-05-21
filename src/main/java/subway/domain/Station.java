package subway.domain;

import java.util.Objects;
import subway.exception.station.InvalidStationNameLengthException;

public class Station {
    private static final int NAME_MAX_LENGTH = 10;
    private final String name;

    public Station(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (name.isBlank() || name.length() > NAME_MAX_LENGTH) {
            throw new InvalidStationNameLengthException("역 이름은 1글자 이상, 10글자 이하여야 합니다.");
        }
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
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
