package subway.line.domain.line;

import java.util.Objects;
import org.springframework.util.StringUtils;
import subway.line.domain.line.exception.LineColorException;

public class LineColor {

    private static final int MAX_COLOR_LENGTH = 20;

    private final String value;

    public LineColor(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (!StringUtils.hasText(value)) {
            throw new LineColorException("노선 색상이 공백입니다. 글자를 입력해주세요");
        }
        if (value.length() > MAX_COLOR_LENGTH) {
            throw new LineColorException("노선 색상이 " + MAX_COLOR_LENGTH + "글자를 초과했습니다");
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
        LineColor lineColor = (LineColor) o;
        return Objects.equals(value, lineColor.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
