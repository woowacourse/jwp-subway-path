package subway.domain;

import java.util.Objects;

public class Color {

    private final String color;

    public Color(final String color) {
        validateColor(color);
        this.color = color;
    }

    private void validateColor(final String color) {
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("색깔이 입력되지 않았습니다.");
        }
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Color color1 = (Color) o;
        return Objects.equals(color, color1.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }

    @Override
    public String toString() {
        return "Color{" +
                "color='" + color + '\'' +
                '}';
    }
}
