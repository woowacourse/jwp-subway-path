package subway.line.domain;

import subway.station.exception.NameLengthException;

import java.util.Objects;

public class LineName {

    private static final int MINIMUM_NAME_LENGTH = 2;
    private static final int MAXIMUM_NAME_LENGTH = 15;

    private final String name;

    public LineName(String name) {
        String stripped = name.strip();
        validateNameLength(stripped);
        this.name = name;
    }

    private void validateNameLength(String stripped) {
        if (stripped.length() < MINIMUM_NAME_LENGTH || stripped.length() > MAXIMUM_NAME_LENGTH) {
            throw new NameLengthException("이름 길이는 " + MINIMUM_NAME_LENGTH + "자 이상 " + MAXIMUM_NAME_LENGTH + "자 이하입니다.");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineName lineName = (LineName) o;
        return Objects.equals(name, lineName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
