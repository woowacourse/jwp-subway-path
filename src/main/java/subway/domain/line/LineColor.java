package subway.domain.line;

public class LineColor {

    private final String color;

    public LineColor(final String color) {
        validate(color);
        this.color = color;
    }

    private void validate(final String color) {
        if (color.isBlank()) {
            throw new IllegalArgumentException("호선 색깔은 공백을 입력할 수 없습니다.");
        }

        if (color.length() < 5 || color.length() > 20) {
            throw new IllegalArgumentException("호선 색깔은 5자 이상 20자 이하만 가능합니다.");
        }
    }

    public String getColor() {
        return color;
    }
}
