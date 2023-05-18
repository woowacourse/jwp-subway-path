package subway.vo;

import java.util.Objects;

public class Color {

    private final String color;

    private Color(final String color) {
        this.color = color;
    }

    public static Color from(String color) {
        return new Color(color);
    }

    public String getValue() {
        return color;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Color color1 = (Color) o;
        return Objects.equals(color, color1.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }

}
