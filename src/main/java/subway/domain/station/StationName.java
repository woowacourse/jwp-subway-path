package subway.domain.station;

import java.util.Objects;

public class StationName {

    private static final int MAX_STATION_NAME = 10;
    private final String name;

    public StationName(final String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(final String name) {
        validateEmptyName(name);
        validateMaxLength(name);
    }

    private void validateEmptyName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("역 이름은 공백일 수 없습니다.");
        }
    }

    private void validateMaxLength(final String name) {
        if (name.length() > MAX_STATION_NAME) {
            throw new IllegalArgumentException("역 이름은 " + MAX_STATION_NAME + "글자 보다 작아야합니다.");
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
        final StationName that = (StationName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
