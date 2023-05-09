package subway.domain;

import subway.exception.GlobalException;

public class LineInfo {
    private static final int NAME_MIN_LENGTH = 1;
    private static final int NAME_MAX_LENGTH = 10;

    private final String name;

    public LineInfo(final String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(final String name) {
        if (name == null || name.length() < NAME_MIN_LENGTH || NAME_MAX_LENGTH < name.length()) {
            throw new GlobalException("호선은 이름은 1글자 이상, 10글자 이하여야 한다.");
        }
    }
}
