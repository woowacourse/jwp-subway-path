package subway.domain;

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

    public void insertStation(
            final Long insertedStationId,
            final Long adjacentStationId,
            final LineDirection adjacentToInsertedStationDirection,
            final int distance
    ) {
        validateIsNotExist(insertedStationId);
        validateAdjacentDirection(adjacentToInsertedStationDirection);

        if (adjacentToInsertedStationDirection == LineDirection.UP) {
            insertUpStation(insertedStationId, adjacentStationId, distance);
        }
        if (adjacentToInsertedStationDirection == LineDirection.DOWN) {
            insertDownStation(insertedStationId, adjacentStationId, distance);
        }
    }

    private void validateIsNotExist(final Long insertStationId) {
        if (contains(insertStationId)) {
            throw new StationAlreadyExistsException();
        }
    }

    private static void validateAdjacentDirection(final LineDirection adjacentToInsertedStationId) {
        if (adjacentToInsertedStationId == null) {
            throw new IllegalArgumentException("인접한 방향이 null일 수 없습니다.");
        }
    }

    private void insertUpStation(
            final Long insertStationId,
            final Long adjacentStationId,
            final int distance
    ) {
        final StationEdge adjacentDownStationEdge = getStationEdge(adjacentStationId);
        final int adjacentDownStationIndex = stationEdges.indexOf(adjacentDownStationEdge);

        final List<StationEdge> splitEdges = adjacentDownStationEdge.splitFromDownStation(insertStationId, distance);
        stationEdges.remove(adjacentDownStationIndex);
        stationEdges.addAll(adjacentDownStationIndex, splitEdges);
    }

    private void insertDownStation(
            final Long insertStationId,
            final Long adjacentStationId,
            final int distance
    ) {
        final StationEdge adjacentUpStationEdge = getStationEdge(adjacentStationId);
        if (isLastEdge(adjacentUpStationEdge)) {
            insertToLastEdge(insertStationId, distance);
            return;
        }

        final int adjacentDownStationIndex = stationEdges.indexOf(adjacentUpStationEdge) + 1;
        final StationEdge adjacentDownStationEdge = stationEdges.get(adjacentDownStationIndex);

        final int distanceFromDown = adjacentDownStationEdge.getDistance() - distance;

        final List<StationEdge> splitEdges = adjacentDownStationEdge.splitFromDownStation(insertStationId, distanceFromDown);
        stationEdges.remove(adjacentDownStationIndex);
        stationEdges.addAll(adjacentDownStationIndex, splitEdges);
    }

    private boolean isLastEdge(final StationEdge stationEdge) {
        return (stationEdges.indexOf(stationEdge) + 1) == stationEdges.size();
    }

    private void insertToLastEdge(final Long insertStationId, final int distance) {
        StationEdge insertedStationEdge = new StationEdge(insertStationId, distance);
        stationEdges.add(insertedStationEdge);
    }

    public StationEdge deleteStation(final Long stationId) {
        validateIsExist(stationId);

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

    private void validateIsExist(final Long stationId) {
        if (!contains(stationId)) {
            throw new StationNotFoundException();
        }
    }

    public int size() {
        return stationEdges.size();
    }

    private StationEdge getStationEdge(final Long stationId) {
        return stationEdges.stream()
                .filter(edge -> edge.getDownStationId().equals(stationId))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    public boolean contains(final Long stationId) {
        return stationEdges.stream()
                .anyMatch(stationEdge -> stationEdge.isDownStationId(stationId));
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
                .map(StationEdge::getDownStationId)
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
