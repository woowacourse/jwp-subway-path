package subway.domain.line;

import subway.domain.fare.Fare;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

public class Line {
    private final Long id;
    private final Sections sections;
    private final LineName lineName;
    private final LineColor lineColor;
    private final Fare extraFare;

    public Line(final Sections sections, final LineName lineName, final LineColor lineColor) {
        this(null, sections, lineName, lineColor, new Fare(0));
    }

    public Line(final Sections sections, final LineName lineName, final LineColor lineColor, final Fare fare) {
        this(null, sections, lineName, lineColor, fare);
    }

    public Line(final Long id, final Sections sections, final LineName lineName, final LineColor lineColor) {
        this(id, sections, lineName, lineColor, new Fare(0));
    }

    public Line(final Long id, final Sections sections, final LineName lineName, final LineColor lineColor,
                final Fare extraFare) {
        this.id = id;
        this.sections = sections;
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.extraFare = extraFare;
    }

    public Station getFrontStation() {
        return sections.getFrontStation();
    }

    public Station getEndStation() {
        return sections.getEndStation();
    }

    public void addSection(final Section section) {
        sections.addSection(section);
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

    public Fare getExtraFare() {
        return extraFare;
    }
}
