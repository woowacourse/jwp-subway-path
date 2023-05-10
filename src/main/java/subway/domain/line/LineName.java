package subway.domain.line;

import java.util.Objects;

public class LineName {
    private static final int MAX_NAME_LENGTH = 10;
    private static final int MIN_NAME_LENGTH = 3;

    private final String name;

    public LineName(final String name) {
        validateLength(name);
        this.name = name;
    }

    private void validateLength(final String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("노선 이름 길이는 " + MIN_NAME_LENGTH + "~" + MAX_NAME_LENGTH + "여야 합니다.");
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
        final LineName lineName = (LineName) o;
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
