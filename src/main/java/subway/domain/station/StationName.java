package subway.domain.station;

import java.util.Objects;

public class StationName {

    private final String name;

    public StationName(final String name) {
        validateNameIsBlank(name);
        this.name = name;
    }

    private void validateNameIsBlank(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("역 이름은 공백일 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }
}
