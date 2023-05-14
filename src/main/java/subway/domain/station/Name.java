package subway.domain.station;

import java.util.Objects;
import java.util.regex.Pattern;
import subway.exception.InvalidStationNameException;

final class Name {

    private static final int MAXIMUM_LENGTH = 11;
    private static final Pattern PATTERN = Pattern.compile("^[가-힣0-9]+역$");

    private final String value;

    public Name(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new InvalidStationNameException("역 이름은 공백일 수 없습니다.");
        }
        if (value.length() > MAXIMUM_LENGTH) {
            throw new InvalidStationNameException("역 이름은 " + MAXIMUM_LENGTH + "글자까지 가능합니다.");
        }
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidStationNameException("역 이름은 한글, 숫자만 가능하고, '역'으로 끝나야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
