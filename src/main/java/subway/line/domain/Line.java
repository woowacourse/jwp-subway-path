package subway.line.domain;

import java.util.List;
import subway.section.domain.Section;
import subway.section.domain.Sections;

public class Line {

    private final Long id;
    private final String lineName;
    private final Sections sections;

    public Line(Long id, String lineName, Sections sections) {
        this.id = id;
        this.lineName = lineName;
        this.sections = sections;
    }

    public Line(String lineName, Sections sections) {
        this(null, lineName, sections);
    }

    public void initializeLine(final String upStationName, final String downStationName, final int distance) {
        sections.initializeSections(upStationName, downStationName, distance);
    }

    public void addStation(final String upStationName, final String downStationName, final int distance) {
        sections.addSection(upStationName, downStationName, distance);
    }

    public void removeStation(final String stationName) {
        sections.removeStation(lineName);
    }

    public boolean hasSameName(final String name) {
        return this.lineName.equals(name);
    }

    public String getLineName() {
        return lineName;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

}
