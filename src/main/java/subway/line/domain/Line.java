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

    public void add(final Section section) {
        if (sections.isEmpty()) {
            initializeLine(section);
            return;
        }
        addStation(section);
    }

    private void initializeLine(final Section section) {
        sections.initializeSections(section);
    }

    private void addStation(final Section section) {
        sections.addSection(section);
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
