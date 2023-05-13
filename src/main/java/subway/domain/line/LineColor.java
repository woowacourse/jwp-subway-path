package subway.domain.line;

import java.util.regex.Pattern;

public class LineColor {

    private static final Pattern COLOR_PATTERN = Pattern.compile("^bg-[a-z]{3,7}-\\d{2,3}$");

    private final String color;

    private LineColor(final String color) {
        validateColorFormat(color);

        this.color = color;
    }

    private void validateColorFormat(final String color) {
        if (!COLOR_PATTERN.matcher(color).matches()) {
            throw new IllegalArgumentException("노선 색깔은 tailwindcss 형식만 가능합니다");
        }
    }

    public static LineColor from(final String color) {
        return new LineColor(color);
    }

    public String getColor() {
        return color;
    }
}
