package subway.line.domain;

import java.util.List;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.station.domain.Station;

public class Line {

    private final Long id;
    private final String lineName;
    private final Sections sections;

    public Line(String lineName) {
        this(null, lineName, Sections.empty());
    }

    public Line(Long id, String lineName) {
        this(id, lineName, Sections.empty());
    }

    public Line(Long id, String lineName, Sections sections) {
        this.id = id;
        this.lineName = lineName;
        this.sections = sections;
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            initializeLine(section.getUpStation(), section.getDownStation(), section.getDistance());
            return;
        }
        addStation(section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    private void initializeLine(final Station upStation, final Station downStation, final int distance) {
        sections.initializeSections(upStation, downStation, distance);
    }

    private void addStation(final Station upStation, final Station downStation, final int distance) {
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

    public boolean isEmpty() {
        return sections.isEmpty();
    }

}
