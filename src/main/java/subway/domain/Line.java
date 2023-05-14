package subway.domain;

import subway.domain.dto.InsertionResult;
import subway.exception.StationAlreadyExistsException;
import subway.exception.StationNotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Line {

    public static final int UP_END_EDGE_DISTANCE = 0;

    private Long id;
    private String name;
    private String color;
    private List<StationEdge> stationEdges;

    private Line(final String name, final String color, final List<StationEdge> stationEdges) {
        this.id = null;
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

    public static Line of(
            final String name,
            final String color,
            final Long upStationId,
            final Long downStationId,
            final int distance
    ) {
        final StationEdge upEndEdge = new StationEdge(upStationId, UP_END_EDGE_DISTANCE);
        final StationEdge downEndEdge = new StationEdge(downStationId, distance);
        return new Line(name, color, List.of(upEndEdge, downEndEdge));
    }

    public static Line of(final Long id, final String name, final String color, final List<StationEdge> stationEdges) {
        return new Line(id, name, color, stationEdges);
    }

    public InsertionResult insertUpStation(
            final Long insertStationId,
            final Long adjacentStationId,
            final int distance
    ) {
        if (contains(insertStationId)) {
            throw new StationAlreadyExistsException();
        }

        final StationEdge adjacentDownStationEdge = getStationEdge(adjacentStationId);
        int adjacentDownStationIndex = stationEdges.indexOf(adjacentDownStationEdge);

        final List<StationEdge> splitEdges = adjacentDownStationEdge.splitFromDownStation(insertStationId, distance);
        stationEdges.remove(adjacentDownStationIndex);
        stationEdges.addAll(adjacentDownStationIndex, splitEdges);
        return new InsertionResult(splitEdges.get(0), splitEdges.get(1));
    }

    public InsertionResult insertDownStation(
            final Long insertStationId,
            final Long adjacentStationId,
            final int distance
    ) {
        if (contains(insertStationId)) {
            throw new StationAlreadyExistsException();
        }

        final int adjacentDownStationIndex = stationEdges.indexOf(getStationEdge(adjacentStationId)) + 1;

        final boolean isLastEdge = adjacentDownStationIndex == stationEdges.size();
        if (isLastEdge) {
            StationEdge insertedStationEdge = new StationEdge(insertStationId, distance);
            stationEdges.add(insertedStationEdge);
            return new InsertionResult(insertedStationEdge, null);
        }

        final StationEdge adjacentDownStationEdge = stationEdges.get(adjacentDownStationIndex);

        final int distanceFromDown = adjacentDownStationEdge.getDistance() - distance;

        final List<StationEdge> splitEdges = adjacentDownStationEdge.splitFromDownStation(insertStationId, distanceFromDown);
        stationEdges.remove(adjacentDownStationIndex);
        stationEdges.addAll(adjacentDownStationIndex, splitEdges);
        return new InsertionResult(splitEdges.get(0), splitEdges.get(1));
    }

    public StationEdge deleteStation(final Long stationId) {
        if (!contains(stationId)) {
            throw new StationNotFoundException();
        }
        final StationEdge stationEdge = getStationEdge(stationId);

        final int edgeIndex = stationEdges.indexOf(stationEdge);
        stationEdges.remove(stationEdge);
        if (edgeIndex == stationEdges.size()) {
            return null;
        }

        final StationEdge downStationEdge = stationEdges.get(edgeIndex);
        final StationEdge combinedStationEdge = new StationEdge(
                downStationEdge.getDownStationId(),
                stationEdge.getDistance() + downStationEdge.getDistance()
        );
        stationEdges.set(edgeIndex, combinedStationEdge);
        return combinedStationEdge;
    }

    public int size() {
        return stationEdges.size();
    }

    private StationEdge getStationEdge(final Long stationId) {
        return stationEdges.stream()
                .filter(edge -> edge.getDownStationId().equals(stationId))
                .findFirst().orElseThrow(StationNotFoundException::new);
    }

    public boolean contains(final Long stationId) {
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
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
