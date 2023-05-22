package subway.domain.line;

import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

public class Line {

    private Long id;
    private Name name;
    private Color color;
    private Sections sections;

    public Line() {
    }

    public Line(final Long id, final Name name, final Color color, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(final Long id, final String name, final String color, final List<Section> sections) {
        this(id, new Name(name), new Color(color), new Sections(sections));
    }

    public Line(final Long id, final String name, final String color) {
        this(id, new Name(name), new Color(color), new Sections(new ArrayList<>()));
    }

    public Line(final String name, final String color) {
        this(null, new Name(name), new Color(color), new Sections(new ArrayList<>()));
    }

    public Section addInitStations(final Section section) {
        sections.addInitSection(section);
        return section;
    }

    public Sections addStation(final Station baseStation, final String direction, final Station registerStation, final int distance) {
        sections.addSection(baseStation, direction, registerStation, distance);
        return sections;
    }

    public Sections deleteStation(final Station station) {
        sections.deleteSection(station);
        return sections;
    }

    public List<Station> sortStation() {
        return sections.sortStation();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    public Sections getSections() {
        return sections;
    }
}
