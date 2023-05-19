package subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
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
}
