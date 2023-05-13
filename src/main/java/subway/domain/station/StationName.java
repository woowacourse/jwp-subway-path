package subway.domain.station;

import java.util.regex.Pattern;

public class StationName {

    private static final Pattern PATTERN = Pattern.compile("^[가-힣0-9]*$");
    private static final int MINIMUM_LENGTH = 2;
    private static final int MAXIMUM_LENGTH = 9;

    private final String name;

    private StationName(final String name) {
        validateName(name);

        this.name = name;
    }

    public static StationName from(final String name) {
        return new StationName(name);
    }

    private void validateName(final String name) {
        validateFormat(name);
        validateLength(name);
    }

    private void validateLength(final String name) {
        if (!(MINIMUM_LENGTH <= name.length() && name.length() <= MAXIMUM_LENGTH)) {
            throw new IllegalArgumentException("역 이름은 2글자 ~ 9글자만 가능합니다.");
        }
    }

    private void validateFormat(final String name) {
        if (!PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("역 이름은 한글과 숫자만 가능합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
