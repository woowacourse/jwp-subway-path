package subway.domain;

import subway.exception.GlobalException;

import java.util.Objects;

public class Line {
    private static final int NAME_MIN_LENGTH = 1;
    private static final int NAME_MAX_LENGTH = 10;

    private final String name;

    public Line(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.length() < NAME_MIN_LENGTH || NAME_MAX_LENGTH < name.length()) {
            throw new GlobalException("호선의 이름은 1글자 이상, 10글자 이하여야 한다.");
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
        Line line = (Line) o;
        return Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
