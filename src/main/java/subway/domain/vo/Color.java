package subway.domain.vo;

public class Color {

    private final String color;

    private Color(final String color) {
        this.color = color;
    }

    public static Color from(String color) {
        return new Color(color);
    }

    public String getColor() {
        return color;
    }


}
