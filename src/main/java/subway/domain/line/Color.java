package subway.domain.line;

import java.util.Objects;
import java.util.regex.Pattern;
import subway.exception.InvalidColorException;

final class Color {

    private static final Pattern PATTERN = Pattern.compile("^[가-힣]{1,10}색$");

    private final String value;

    public Color(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value)) {
            throw new InvalidColorException("노선 색은 존재해야 합니다.");
        }
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidColorException("색 이름은 '색'으로 끝나야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
