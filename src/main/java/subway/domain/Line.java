package subway.domain;

import java.util.List;

public class Line {

    private Long id;
    private final String name;
    private final String color;
    private Sections sections;

    public Line(final Long id, final String name, final String color, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public Line(final String name, final String color, final Sections sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(Sections sections) {
        this.name = getName();
        this.color = getColor();
        this.sections = sections;
    }

    public Line addSection(Section section) {
        Sections addedSections = sections.addSection(section);
        return new Line(addedSections);
    }

    public Line deleteStation(Station station) {
        Sections deletedSections = sections.deleteSection(station);
        return new Line(deletedSections);
    }

    public List<Station> getRoute() {
        return sections.sortStations();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Sections getSections() {
        return sections;
    }
}
