package subway.domain.station;

import static subway.exception.ErrorCode.STATION_NAME_LENGTH;

import java.util.Objects;
import subway.exception.BadRequestException;

public class StationName {

    private static final int MAX_NAME_LENGTH = 10;
    private static final int MIN_NAME_LENGTH = 3;

    private final String name;

    private StationName(final String name) {
        this.name = name;
    }

    public static StationName create(final String name) {
        validateLength(name);
        return new StationName(name);
    }

    public static StationName empty() {
        return new StationName(null);
    }

    private static void validateLength(final String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new BadRequestException(STATION_NAME_LENGTH);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StationName lineName = (StationName) o;
        return Objects.equals(name, lineName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String name() {
        return name;
    }
}
