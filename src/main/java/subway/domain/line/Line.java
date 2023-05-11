package subway.domain.line;

import subway.domain.section.Sections;

public class Line {
    private final Long id;
    private final Sections sections;
    private final LineName lineName;
    private final LineColor lineColor;

    public Line(final Sections sections, final LineName lineName, final LineColor lineColor) {
        this(null, sections, lineName, lineColor);
    }

    public Line(final Long id, final Sections sections, final LineName lineName, final LineColor lineColor) {
        this.id = id;
        this.sections = sections;
        this.lineName = lineName;
        this.lineColor = lineColor;
    }

    public Long getId() {
        return id;
    }

    public Sections getSections() {
        return sections;
    }

    public String getLineName() {
        return lineName.getName();
    }

    public String getLineColor() {
        return lineColor.getColorValue();
    }
}
