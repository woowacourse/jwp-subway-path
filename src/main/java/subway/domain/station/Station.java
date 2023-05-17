package subway.domain.station;

import java.util.Objects;
import org.springframework.util.Assert;

public class Station {
    private static final int MAX_STATION_NAME_LENGTH = 10;
    private final String name;

    public Station(String name) {
        validateBlank(name);
        validateLength(name);
        validateStationFormat(name);
        this.name = name;
    }

    private void validateBlank(String name) {
        Assert.hasText(name, "역의 이름은 빈 값이 될 수 없습니다.");
    }

    private void validateLength(String name) {
        if (name.length() >= MAX_STATION_NAME_LENGTH) {
            throw new IllegalArgumentException("역의 이름은 " + MAX_STATION_NAME_LENGTH + "글자를 초과할 수 없습니다.");
        }
    }

    private void validateStationFormat(String name) {
        if (!name.endsWith("역")) {
            throw new IllegalArgumentException("역의 이름은 \"역\"으로 끝나야 합니다.");
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
        if (!(o instanceof Station)) {
            return false;
        }

        Station station = (Station) o;

        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
