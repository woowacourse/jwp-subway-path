package subway.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Line {
    private Long id;
    private String name;
    private String color;
    private List<StationEdge> stationEdges;

    private Line(final String name, final String color, final List<StationEdge> stationEdges) {
        this.name = name;
        this.color = color;
        this.stationEdges = stationEdges;
    }

    private Line(final Long id, final String name, final String color, final List<StationEdge> stationEdges) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationEdges = stationEdges;
    }

    public static Line of(final String name, final String color, final StationEdge stationEdge) {
        List<StationEdge> stationEdges = new LinkedList<>();
        stationEdges.add(stationEdge);

        return new Line(name, color, stationEdges);
    }

    public static Line of(final Long id, final String name, final String color, final StationEdge stationEdge) {
        List<StationEdge> stationEdges = new LinkedList<>();
        stationEdges.add(stationEdge);

        return new Line(id, name, color, stationEdges);
    }

    public static Line of(final Long id, final String name, final String color, final List<StationEdge> stationEdges) {
        return new Line(id, name, color, stationEdges);
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

    public List<StationEdge> getStationEdges() {
        return stationEdges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
