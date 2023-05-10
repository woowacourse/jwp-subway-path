package subway.domain.line;

import subway.domain.section.Sections;

public class Line {
    private final Sections sections;
    private final LineName lineName;
    private final LineColor lineColor;

    public Line(final Sections sections, final LineName lineName, final LineColor lineColor) {
        this.sections = sections;
        this.lineName = lineName;
        this.lineColor = lineColor;
    }
}
