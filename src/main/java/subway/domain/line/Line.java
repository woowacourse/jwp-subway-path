package subway.domain.line;

import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.Stations;

public class Line {

    private final Long id;
    private final LineName lineName;
    private final LineColor lineColor;
    private final Sections sections;

    public Line(final LineName lineName, final LineColor lineColor) {
        this(null, lineName, lineColor, Sections.emptySections());
    }

    public Line(final Long id, final LineName lineName, final LineColor lineColor) {
        this(id, lineName, lineColor, Sections.emptySections());
    }

    public Line(final Long id, final LineName lineName, final LineColor lineColor, final Sections sections) {
        validateLineName(lineName);
        validateLineColor(lineColor);
        validateSections(sections);

        this.id = id;
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.sections = sections;
    }

    private void validateLineName(final LineName lineName) {
        if (lineName == null) {
            throw new IllegalArgumentException("노선의 이름은 필수 입니다.");
        }
    }

    private void validateLineColor(final LineColor lineColor) {
        if (lineColor == null) {
            throw new IllegalArgumentException("노선의 색상은 필수 입니다.");
        }
    }

    private void validateSections(final Sections sections) {
        if (sections == null) {
            throw new IllegalArgumentException("노선의 구간들은 빈 구간이라도 필요합니다.");
        }
    }

    public Line addSection(final Section other) {
        return new Line(
                this.id,
                this.lineName,
                this.lineColor,
                this.sections.addSection(other)
        );
    }

    public Line deleteStation(final Station other) {
        return new Line(
                this.id,
                this.lineName,
                this.lineColor,
                this.sections.removeStation(other)
        );
    }

    public Stations getStations() {
        return sections.getStations();
    }

    public boolean isSectionsEmpty() {
        return sections.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public LineName getLineName() {
        return lineName;
    }

    public LineColor getLineColor() {
        return lineColor;
    }

    public Sections getSections() {
        return sections;
    }
}
