package subway.domain.line;

import java.util.regex.Pattern;

public class Color {

    private static final Pattern COLOR_REGEX = Pattern.compile("^#([A-Fa-f0-9]{6})$");

    private final String color;

    public Color(final String color) {
        validateHexColorCode(color);
        this.color = color;
    }

    private void validateHexColorCode(final String color) {
        if (COLOR_REGEX.matcher(color).matches()) {
            return;
        }
        throw new IllegalArgumentException("#을 포함한 7자리 16진수 색상표여야 합니다.");
    }

    public String getColor() {
        return color;
    }
}
