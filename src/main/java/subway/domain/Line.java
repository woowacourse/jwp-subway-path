package subway.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Line {

    public static final int UP_END_EDGE_DISTANCE = 0;

    private Long id;
    private String name;
    private String color;
    private StationEdges stationEdges;

    private Line(final String name, final String color, final List<StationEdge> stationEdges) {
        this.name = name;
        this.color = color;
        this.stationEdges = new StationEdges(new LinkedList<>(stationEdges));

    }

    private Line(final Long id, final String name, final String color, final List<StationEdge> stationEdges) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationEdges = new StationEdges(new LinkedList<>(stationEdges));
    }

    public static Line of(final String name, final String color, final Long upStationId, final Long downStationId, final int distance) {
        final StationEdge upEndEdge = new StationEdge(upStationId, UP_END_EDGE_DISTANCE);
        final StationEdge downEndEdge = new StationEdge(downStationId, distance);
        return new Line(name, color, List.of(upEndEdge, downEndEdge));
    }

    public static Line of(final Long id, final String name, final String color, final List<StationEdge> stationEdges) {
        return new Line(id, name, color, stationEdges);
    }

    public void addStationUpperFrom(
            Long insertStationId,
            long adjacentStationId,
            Distance distance
    ) {
        stationEdges.addStationUpperFrom(insertStationId, adjacentStationId, distance);
    }

    public void addStationDownFrom(
            Long insertStationId,
            Long adjacentStationId,
            Distance distance
    ) {
        stationEdges.addStationDownFrom(insertStationId, adjacentStationId, distance);
    }

    public StationEdge deleteStation(long stationId) {
        return stationEdges.deleteStation(stationId);
    }

    public boolean canDeleteStation() {
        return stationEdges.canDeleteStation();
    }

    public boolean contains(Long stationId) {
        return stationEdges.contains(stationId);
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
        return stationEdges.getStationEdges();
    }

    public List<Long> getStationIds() {
        return stationEdges.getStationIds();
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
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
