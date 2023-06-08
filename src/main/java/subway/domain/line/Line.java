package subway.domain.line;

import subway.domain.LineDirection;
import subway.domain.line.edge.StationEdge;
import subway.domain.line.edge.StationEdges;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Line {

    private Long id;
    private String name;
    private String color;
    private StationEdges stationEdges;

    private Line(final String name, final String color, final StationEdges stationEdges) {
        this.id = null;
        this.name = name;
        this.color = color;
        this.stationEdges = stationEdges;
    }

    private Line(final Long id, final String name, final String color, final StationEdges stationEdges) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationEdges = stationEdges;
    }

    public static Line of(
            final String name,
            final String color,
            final Long upStationId,
            final Long downStationId,
            final int distance
    ) {
        final StationEdge initialStationEdge = new StationEdge(upStationId, downStationId, distance);
        return new Line(name, color, new StationEdges(Set.of(initialStationEdge)));
    }

    public static Line of(
            final Long id,
            final String name,
            final String color,
            final Set<StationEdge> initialStationEdges
    ) {
        return new Line(id, name, color, new StationEdges(initialStationEdges));
    }

    public void insertStation(
            final Long insertedStationId,
            final Long adjacentStationId,
            final LineDirection adjacentToInsertedStationDirection,
            final int distance
    ) {
        stationEdges.addStation(insertedStationId, adjacentStationId, adjacentToInsertedStationDirection, distance);
    }

    public void removeStation(final Long stationId) {
        stationEdges.removeStation(stationId);
    }

    public int size() {
        return stationEdges.size();
    }

    public boolean contains(final Long stationId) {
        return stationEdges.isContainStation(stationId);
    }

    public boolean contains(final Long station1Id, final Long station2Id) {
        return stationEdges.contains(station1Id, station2Id) || stationEdges.contains(station2Id, station1Id);
    }

    public int getEdgeDistanceBetween(final Long station1Id, final Long station2Id) {
        return stationEdges.getEdgeDistanceBetween(station1Id, station2Id);
    }

    public boolean isSameName(final String name) {
        return this.name.equals(name);
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

    public StationEdges getStationEdges() {
        return stationEdges;
    }

    public List<Long> getStationIdsByOrder() {
        return stationEdges.getStationIdsInOrder();
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
