package subway.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import subway.domain.dto.InsertionResult;
import subway.exception.StationAlreadyExistsException;
import subway.exception.StationNotFoundException;

public class Line {
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

    public static Line of(final String name, final String color, final List<StationEdge> stationEdges) {
        return new Line(name, color, stationEdges);
    }

    public static Line of(final Long id, final String name, final String color, final List<StationEdge> stationEdges) {
        return new Line(id, name, color, stationEdges);
    }

    // TODO: 리팩터링 (방향별로 메서드 분리)
    public InsertionResult insertStation(
            Long insertStationId,
            Long adjacentStationId,
            int distance,
            LineDirection direction
    ) {
        if (contains(insertStationId)) {
            throw new StationAlreadyExistsException();
        }
        final int targetIndex = getTargetStationEdge(adjacentStationId, direction);
        if (targetIndex == stationEdges.size()) { // 하행 종점 추가일 경우
            StationEdge insertedStationEdge = new StationEdge(insertStationId, distance);
            stationEdges.add(insertedStationEdge);
            return new InsertionResult(insertedStationEdge, null);
        }
        StationEdge targetStationEdge = stationEdges.get(targetIndex);

        int distanceFromDown =
                (direction == LineDirection.DOWN) ? targetStationEdge.getDistance() - distance : distance;
        List<StationEdge> splitEdges = targetStationEdge.split(insertStationId, distanceFromDown);
        stationEdges.remove(targetIndex);
        stationEdges.addAll(targetIndex, splitEdges);
        return new InsertionResult(splitEdges.get(0), splitEdges.get(1));
    }

    private int getTargetStationEdge(Long adjacentStationId, LineDirection direction) {
        StationEdge stationEdge = getStationEdge(adjacentStationId);

        int order = stationEdges.indexOf(stationEdge);
        if (direction == LineDirection.UP) {
            return order;
        }
        return order + 1;
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
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
