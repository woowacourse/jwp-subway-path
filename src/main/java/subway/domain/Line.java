package subway.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Line {
    private final Long id;
    private final Sections sections;
    private String name;
    private String color;

    public Line(final Long id, final String name, final String color, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(final Long id, final String name, final String color) {
        this(id, name, color, new Sections());
    }


    public void addInitStations(final Station up, final Station down, final int distance) {
        sections.addInitStations(up, down, new Distance(distance));
    }


    public void addUpEndpoint(final Station station, final int distance) {
        sections.addUpEndpoint(station, new Distance(distance));
    }

    public void addDownEndpoint(final Station station, final int distance) {
        sections.addDownEndpoint(station, new Distance(distance));
    }

    public void addIntermediate(final Station station, final Station prevStation, final int distance) {
        sections.addIntermediate(station, prevStation, new Distance(distance));
    }

    public void deleteSections(final Station station) {
        sections.delete(station);
    }

    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> getAllStations() {
        return sections.getAllStations();
    }

    public List<Integer> getAllDistances() {
        return sections.getAllDistances().stream()
                .map(Distance::getValue)
                .collect(Collectors.toList());
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

    public List<Section> getSections() {
        return sections.getSections();
    }
}
