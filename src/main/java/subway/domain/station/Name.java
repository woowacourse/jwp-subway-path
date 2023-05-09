package subway.domain.station;

import java.util.Objects;
import java.util.regex.Pattern;
import subway.exception.InvalidStationNameException;

final class Name {

    private static final int MINIMUM_LENGTH = 2;
    private static final int MAXIMUM_LENGTH = 11;
    private static final String SUFFIX = "역";
    private static final Pattern PATTERN = Pattern.compile("^[가-힣0-9]+$");

    private final String value;

    public Name(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new InvalidStationNameException("역 이름은 공백일 수 없습니다.");
        }
        if (MINIMUM_LENGTH > value.length() || value.length() > MAXIMUM_LENGTH) {
            throw new InvalidStationNameException("역 이름은 2글자에서 11글자까지 가능합니다.");
        }
        if (!value.endsWith(SUFFIX)) {
            throw new InvalidStationNameException("역 이름은 '역'으로 끝나야 합니다.");
        }
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidStationNameException("역 이름은 한글, 숫자만 가능합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
