package subway.domain;

import java.util.*;

public class Line {

    private final Long id;
    private final LineName name;
    private final LineColor color;
    private final Sections sections;

    public Line(final LineName name, final LineColor color) {
        this(null, name, color, new Sections(new ArrayList<>()));
    }

    public Line(final LineName name, final LineColor color, final Sections sections) {
        this(null, name, color, sections);
    }

    public Line(final Long id, final LineName name, final LineColor color, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line addSection(final Section section) {
        Sections updateSections = sections.addSection(section);
        return new Line (id, name, color, updateSections);
    }

    public Line removeStation(final Station station) {
        Sections updateSections = sections.removeStation(station);
        return new Line(id, name, color, updateSections);
    }

    public List<Station> findAllStation() {
        return sections.findAllStationUpToDown();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getColor() {
        return color.getValue();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public Station getFirstStation() {
        return sections.getFirstStation();
    }

    public Station getLastStation() {
        return sections.getLastStation();
    }
}
