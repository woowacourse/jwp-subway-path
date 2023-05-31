package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private final Long id;
    private final String name;
    private final Sections sections;

    private Line(Long id, String name, Sections sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public static Line of(Long id, String name, List<Section> sections) {
        if (sections.isEmpty()) {
            return new Line(id, name, new Sections(sections));
        }
        return new Line(id, name, Sections.from(sections));
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        Section newSection = new Section(upStation, downStation, distance);
        sections.addSection(newSection);
    }

    public void deleteSection(Station station) {
        sections.deleteSection(station);
    }

    public Long getId() {
        return id;
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
}
