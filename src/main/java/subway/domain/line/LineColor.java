package subway.domain.line;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;
import subway.exception.line.LineColorException;

@Getter
@ToString
@EqualsAndHashCode(of = "value")
public class LineColor {

    private static final int MAX_COLOR_LENGTH = 20;

    private final String value;

    public LineColor(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new LineColorException("노선 색상이 공백입니다. 글자를 입력해주세요");
        }
        if (value.length() > MAX_COLOR_LENGTH) {
            throw new LineColorException("노선 색상이 " + MAX_COLOR_LENGTH + "글자를 초과했습니다");
        }
    }
}
