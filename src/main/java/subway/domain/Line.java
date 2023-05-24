package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Line {

    public static final int UP_END_EDGE_DISTANCE = 0;

    private final Long id;
    private final String name;
    private final String color;
    private final StationEdges stationEdges;

    public Line(String name, String color, StationEdges stationEdges) {
        this.id = null;
        this.name = name;
        this.color = color;
        this.stationEdges = stationEdges;
    }

    public Line(final Long id, final String name, final String color, final StationEdges stationEdges) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationEdges = stationEdges;
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
        return new ArrayList<>(stationEdges.getStationEdges());
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
