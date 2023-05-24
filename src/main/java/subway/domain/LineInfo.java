package subway.domain;

import java.util.Objects;
import subway.exception.line.InvalidLineNameLengthException;

public class LineInfo {
    private static final int NAME_MIN_LENGTH = 1;
    private static final int NAME_MAX_LENGTH = 10;

    private final String name;

    public LineInfo(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.length() < NAME_MIN_LENGTH || NAME_MAX_LENGTH < name.length()) {
            throw new InvalidLineNameLengthException("호선은 이름은 1글자 이상, 10글자 이하여야 한다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineInfo lineInfo = (LineInfo) o;
        return Objects.equals(name, lineInfo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
