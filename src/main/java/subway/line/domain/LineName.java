package subway.line.domain;

import java.util.Objects;
import org.springframework.util.StringUtils;
import subway.line.domain.exception.LineNameException;

public class LineName {

    private static final int MAX_LINE_NAME_LENGTH = 255;

    private final String value;

    public LineName(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String name) {
        if (!StringUtils.hasText(name)) {
            throw new LineNameException("노선 이름이 공백입니다. 글자를 입력해주세요");
        }
        if (name.length() > MAX_LINE_NAME_LENGTH) {
            throw new LineNameException("노선 이름이 " + MAX_LINE_NAME_LENGTH + "글자를 초과했습니다");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineName lineName = (LineName) o;
        return Objects.equals(value, lineName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
