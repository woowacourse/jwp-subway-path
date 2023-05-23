package subway.domain.station;

import java.util.Objects;

public class StationName {

    private static final int MAX_LENGTH = 20;
    private final String value;

    public StationName(final String name) {
        validateName(name);
        this.value = name;
    }

    private void validateName(final String name) {
        if (MAX_LENGTH < name.length()) {
            throw new IllegalArgumentException("이름의 길이는 " + MAX_LENGTH + "자 이하여야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StationName that = (StationName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
