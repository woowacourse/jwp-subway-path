package subway.domain.line;

import java.util.regex.Pattern;

public class LineName {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[가-힣0-9]*$");
    private static final int MINIMUM_LENGTH = 2;
    private static final int MAXIMUM_LENGTH = 9;

    private final String name;

    private LineName(final String name) {
        validateName(name);

        this.name = name;
    }

    private void validateName(final String name) {
        validateNameLength(name);
        validateNameFormat(name);
    }

    private void validateNameFormat(final String name) {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("노선 이름은 한글과 숫자만 가능합니다.");
        }
    }

    private void validateNameLength(final String name) {
        if (!(MINIMUM_LENGTH <= name.length() && name.length() <= MAXIMUM_LENGTH)) {
            throw new IllegalArgumentException("노선 이름은 2글자 ~ 9글자만 가능합니다.");
        }
    }

    public static LineName from(String name) {
        return new LineName(name);
    }

    public String getName() {
        return name;
    }
}
