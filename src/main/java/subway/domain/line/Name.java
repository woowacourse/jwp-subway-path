package subway.domain.line;

import java.util.Objects;
import java.util.regex.Pattern;
import subway.exception.InvalidLineNameException;

final class Name {

    private static final Pattern PATTERN = Pattern.compile("^[1-9]호선$");

    private final String value;

    public Name(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new InvalidLineNameException("노선 이름은 존재해야 합니다.");
        }
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidLineNameException("노선 이름은 1~9호선이어야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
