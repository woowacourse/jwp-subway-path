package subway.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import subway.domain.dto.InsertionResult;
import subway.exception.StationAlreadyExistsException;
import subway.exception.StationNotFoundException;

public class Line {

    public static final int UP_END_EDGE_DISTANCE = 0;

    private Long id;
    private String name;
    private String color;
    private List<StationEdge> stationEdges;

    private Line(final String name, final String color, final List<StationEdge> stationEdges) {
        this.name = name;
        this.color = color;
        this.stationEdges = new LinkedList<>(stationEdges);
    }

    private Line(final Long id, final String name, final String color, final List<StationEdge> stationEdges) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationEdges = new LinkedList<>(stationEdges);
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
            int distance
    ) {
        if (contains(insertStationId)) {
            throw new StationAlreadyExistsException();
        }
        
        StationEdge adjacentDownStationEdge = getStationEdge(adjacentStationId);
        int adjacentDownStationIndex = stationEdges.indexOf(adjacentDownStationEdge);

        List<StationEdge> splitEdges = adjacentDownStationEdge.splitFromDownStation(insertStationId, distance);
        stationEdges.remove(adjacentDownStationIndex);
        stationEdges.addAll(adjacentDownStationIndex, splitEdges);
    }

    public void addStationDownFrom(
            Long insertStationId,
            Long adjacentStationId,
            int distance
    ) {
        if (contains(insertStationId)) {
            throw new StationAlreadyExistsException();
        }

        final int adjacentDownStationIndex = stationEdges.indexOf(getStationEdge(adjacentStationId)) + 1;

        boolean isLastEdge = adjacentDownStationIndex == stationEdges.size();
        if (isLastEdge) {
            StationEdge insertedStationEdge = new StationEdge(insertStationId, distance);
            stationEdges.add(insertedStationEdge);
            return;
        }
        
        StationEdge adjacentDownStationEdge = stationEdges.get(adjacentDownStationIndex);

        int distanceFromDown = adjacentDownStationEdge.getDistance() - distance;

        List<StationEdge> splitEdges = adjacentDownStationEdge.splitFromDownStation(insertStationId, distanceFromDown);
        stationEdges.remove(adjacentDownStationIndex);
        stationEdges.addAll(adjacentDownStationIndex, splitEdges);
    }

    public StationEdge deleteStation(long stationId) {
        if (!contains(stationId)) {
            throw new StationNotFoundException();
        }
        StationEdge stationEdge = getStationEdge(stationId);

        int edgeIndex = stationEdges.indexOf(stationEdge);
        stationEdges.remove(stationEdge);
        if (edgeIndex == stationEdges.size()) {
            return null;
        }

        StationEdge downStationEdge = stationEdges.get(edgeIndex);
        StationEdge combinedStationEdge = new StationEdge(downStationEdge.getDownStationId(),
                stationEdge.getDistance() + downStationEdge.getDistance());
        stationEdges.set(edgeIndex, combinedStationEdge);
        return combinedStationEdge;
    }

    public int size() {
        return stationEdges.size();
    }

    private StationEdge getStationEdge(long stationId) {
        return stationEdges.stream()
                .filter(edge -> edge.getDownStationId().equals(stationId))
                .findFirst().orElseThrow(StationNotFoundException::new);
    }

    public boolean contains(Long stationId) {
        return stationEdges.stream()
                .anyMatch(stationEdge -> stationEdge.getDownStationId().equals(stationId));
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

    public List<Long> getStationIds() {
        return stationEdges.stream()
                .map(stationEdge -> stationEdge.getDownStationId())
                .collect(Collectors.toList());
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
