package subway.domain;

import java.util.List;

public class Line {
    private final String name;
    private final Sections sections;

    public Line(final String name, final Sections sections) {
        this.name = name;
        this.sections = sections;
    }

    public static Line of(final String name, final List<Section> sections) {
        if (sections.isEmpty()) {
            return new Line(name, new Sections(sections));
        }
        return new Line(name, Sections.from(sections));
    }

    public String getName() {
        return name;
    }

    public List<Section> getSectionsByList() {
        return sections.getSections();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        Section newSection = new Section(upStation, downStation, distance);
        sections.addSection(newSection);
    }
}
