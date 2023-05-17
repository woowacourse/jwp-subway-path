package subway.domain;

import subway.dto.request.LineRequest;

import java.util.List;
import java.util.stream.Collectors;

public class Line {
    private final Long id;
    private String name;
    private String color;
    private final Sections sections;

    public Line(Long id, String name, String color, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(LineRequest request) {
        this(null, request.getName(), request.getColor(), new Sections());
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, new Sections());
    }


    public void addInitStations(Station up, Station down, int distance) {
        sections.addInitStations(up, down, new Distance(distance));
    }


    public void addUpEndpoint(Station station, int distance) {
        sections.addUpEndpoint(station, new Distance(distance));
    }

    public void addDownEndpoint(Station station, int distance) {
        sections.addDownEndpoint(station, new Distance(distance));
    }

    public void addIntermediate(Station station, Station prevStation, int distance) {
        sections.addIntermediate(station, prevStation, new Distance(distance));
    }

    public void deleteSections(Station station) {
        sections.delete(station);
    }

    public void update(String name, String color) {
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
