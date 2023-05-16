package subway.domain.line;

import java.util.Objects;

public class LineColor {

    private final String lineColor;

    private LineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public static LineColor from(String lineColor) {
        return new LineColor(lineColor);
    }

    public String getLineColor() {
        return lineColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineColor other = (LineColor) o;
        return Objects.equals(lineColor, other.lineColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineColor);
    }
}
