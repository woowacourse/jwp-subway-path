package subway.domain.line;

import java.util.Objects;

public class Color {
    private final String color;

    public Color(final String color) {
        validateNull(color);
        this.color = color;
    }

    private void validateNull(final String color) {
        if (color.isBlank()) {
            throw new IllegalArgumentException("호선의 색상은 빈 문자열일 수 없습니다.");
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
        Color color1 = (Color) o;
        return Objects.equals(color, color1.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
