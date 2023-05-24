package subway.domain;

import org.springframework.util.ObjectUtils;

public class Color {

    private final String color;

    private Color(String color) {
        if (ObjectUtils.isEmpty(color)) {
            throw new IllegalStateException("색깔은 빈 값일 수 없습니다.");
        }
        this.color = color;
    }

    public static Color from(final String color) {
        return new Color(color);
    }

    public String getColor() {
        return color;
    }
}
