package subway.domain.common;

import subway.exception.ColorNotBlankException;

public class Color {

    private final String color;

    public Color(final String color) {
        validateColor(color);
        this.color = color;
    }

    private void validateColor(final String color) {
        if (color.isBlank() || color.isEmpty()) {
            throw new ColorNotBlankException();
        }
    }

    public String getColor() {
        return color;
    }
}
