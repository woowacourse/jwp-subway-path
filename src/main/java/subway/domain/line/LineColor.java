package subway.domain.line;

public class LineColor {

    private final String color;

    public LineColor(final String color) {
        validateLineColor(color);
        this.color = color;
    }

    private void validateLineColor(final String color) {
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("노선 색상은 필수입니다.");
        }
    }

    public String color() {
        return color;
    }
}
