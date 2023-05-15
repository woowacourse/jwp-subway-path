package subway.domain.station;

import java.util.Objects;
import java.util.regex.Pattern;
import subway.exception.InvalidStationNameException;

final class Name {

    private static final Pattern PATTERN = Pattern.compile("^[가-힣1-9]{2,11}역$");

    private final String value;

    public Name(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new InvalidStationNameException("역 이름은 공백일 수 없습니다.");
        }
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidStationNameException("역 이름은 2 ~ 11자의 한글과 숫자로 구성되며, '역'으로 끝나야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
