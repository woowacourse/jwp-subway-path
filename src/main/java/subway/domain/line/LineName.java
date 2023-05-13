package subway.domain.line;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;
import subway.exception.line.LineNameException;

@Getter
@ToString
@EqualsAndHashCode(of = "value")
public class LineName {

    private static final int MAX_LINE_LENGTH = 255;

    private final String value;

    public LineName(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new LineNameException("노선 이름이 공백입니다. 글자를 입력해주세요");
        }
        if (name.length() > MAX_LINE_LENGTH) {
            throw new LineNameException("노선 이름이 " + MAX_LINE_LENGTH + "글자를 초과했습니다");
        }
    }
}
