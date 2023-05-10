package subway.domain;

import java.util.List;

public class Line {

    private final String name;
    private final Sections sections;

    public Line(final String name, final Sections sections) {
        this.name = name;
        this.sections = sections;
    }

    public void addSection(final Section section) {
        sections.addSection(section);
    }

    public void removeStation(final Station station) {
        sections.removeStation(station);
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }
}
