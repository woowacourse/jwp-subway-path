package subway.domain.line;

public class Line {

    private final LineName lineName;
    private final LineColor lineColor;

    public Line(final LineName lineName, final LineColor lineColor) {
        this.lineName = lineName;
        this.lineColor = lineColor;
    }

    public LineName getLineName() {
        return lineName;
    }

    public LineColor getLineColor() {
        return lineColor;
    }
}
