package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private final String name;
    private final Sections sections;

    private Line(String name, Sections sections) {
        this.name = name;
        this.sections = sections;
    }

    public static Line of(String name, List<Section> sections) {
        if (sections.isEmpty()) {
            return new Line(name, new Sections(sections));
        }
        return new Line(name, Sections.from(sections));
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        Section newSection = new Section(upStation, downStation, distance);
        sections.addSection(newSection);
    }

    public void deleteSection(Station station) {
        sections.deleteSection(station);
    }

    public String getName() {
        return name;
    }

    public List<Section> getSectionsByList() {
        return new ArrayList<>(sections.getSections());
    }

    public List<Station> getStations() {
        return new ArrayList<>(sections.getStations());
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }
}
