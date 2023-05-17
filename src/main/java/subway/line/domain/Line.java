package subway.line.domain;

import java.util.List;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.station.domain.Station;

public class Line {

    private final Long id;
    private final String lineName;
    private final Sections sections;

    public Line(Long id, String lineName, Sections sections) {
        this.id = id;
        this.lineName = lineName;
        this.sections = sections;
    }

    public Line(String lineName) {
        this(null, lineName, Sections.empty());
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void initializeLine(final Station upStation, final Station downStation, final int distance) {
        sections.initializeSections(upStation, downStation, distance);
    }

    public void addStation(final Station upStation, final Station downStation, final int distance) {
        sections.addSection(upStation, downStation, distance);
    }

    public void removeStation(final Station station) {
        sections.removeStation(station);
    }

    public String getLineName() {
        return lineName;
    }

    public Long getId() {
        return id;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

}
