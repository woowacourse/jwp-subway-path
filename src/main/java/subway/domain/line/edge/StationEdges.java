package subway.domain.line.edge;

import subway.domain.LineDirection;
import subway.exception.IllegalStationEdgeStateException;
import subway.exception.StationAlreadyExistsException;
import subway.exception.StationEdgeNotFoundException;
import subway.exception.StationNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class StationEdges {

    private final Set<StationEdge> stationEdges;

    public StationEdges(final Collection<StationEdge> stationEdges) {
        this.stationEdges = new HashSet<>(stationEdges);
    }

    public static StationEdges from(final StationEdge stationEdge) {
        return new StationEdges(Set.of(stationEdge));
    }

    public void addStation(
            final Long insertedStationId,
            final Long adjacentStationId,
            final LineDirection adjacentToInsertedDirection,
            final int distance
    ) {
        validateIsNotContainStation(insertedStationId);
        validateIsContainStation(adjacentStationId);

        findStationEdgeToSplit(adjacentStationId, adjacentToInsertedDirection).ifPresentOrElse(
                target -> insertStationToMiddle(insertedStationId, target, adjacentToInsertedDirection, distance),
                () -> insertEndStation(insertedStationId, adjacentToInsertedDirection, distance)
        );
    }

    private void validateIsNotContainStation(final Long stationId) {
        if (isContainStation(stationId)) {
            throw new StationAlreadyExistsException();
        }
    }

    private void validateIsContainStation(final Long stationId) {
        if (!isContainStation(stationId)) {
            throw new StationNotFoundException();
        }
    }

    private Optional<StationEdge> findStationEdgeToSplit(
            final Long adjacentStationId,
            final LineDirection adjacentToInsertedDirection
    ) {
        if (adjacentToInsertedDirection == LineDirection.UP) {
            return stationEdges.stream()
                    .filter(edge -> edge.isDownStationId(adjacentStationId))
                    .findFirst();
        }
        return stationEdges.stream()
                .filter(edge -> edge.isUpStationId(adjacentStationId))
                .findFirst();
    }

    private void insertStationToMiddle(
            final Long insertedStationId,
            final StationEdge targetEdge,
            final LineDirection adjacentToInsertedDirection,
            final int distance
    ) {

        final Set<StationEdge> insertedEdges = splitEdge(insertedStationId, targetEdge, adjacentToInsertedDirection, distance);
        stationEdges.remove(targetEdge);
        stationEdges.addAll(insertedEdges);
    }

    private Set<StationEdge> splitEdge(
            final Long insertedStationId,
            final StationEdge targetEdge,
            final LineDirection adjacentToInsertedDirection,
            final int distance
    ) {
        if (adjacentToInsertedDirection == LineDirection.UP) {
            return targetEdge.splitFromDown(insertedStationId, distance);
        }
        return targetEdge.splitFromUp(insertedStationId, distance);
    }

    private void insertEndStation(
            final Long insertedStationId,
            final LineDirection adjacentToInsertedDirection,
            final int distance
    ) {
        if (adjacentToInsertedDirection == LineDirection.UP) {
            // up end
            final Long originalUpEndStation = findUpEndStationId();
            stationEdges.add(new StationEdge(insertedStationId, originalUpEndStation, distance));
        }
        if (adjacentToInsertedDirection == LineDirection.DOWN) {
            // down end
            final Long originalDownEndStationId = findDownEndStationId();
            stationEdges.add(new StationEdge(originalDownEndStationId, insertedStationId, distance));
        }
    }

    public Long findUpEndStationId() {
        final Set<Long> upStationIds = getUpStationIds();
        final Set<Long> downStationIds = getDownStationIds();
        upStationIds.removeAll(downStationIds);
        return upStationIds.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStationEdgeStateException("상행종점역을 찾을 수 없습니다."));
    }

    private Set<Long> getUpStationIds() {
        return stationEdges.stream()
                .map(StationEdge::getUpStationId)
                .collect(Collectors.toSet());
    }

    private Set<Long> getDownStationIds() {
        return stationEdges.stream()
                .map(StationEdge::getDownStationId)
                .collect(Collectors.toSet());
    }

    public Long findDownEndStationId() {
        final Set<Long> upStationIds = getUpStationIds();
        final Set<Long> downStationIds = getDownStationIds();
        downStationIds.removeAll(upStationIds);
        return downStationIds.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStationEdgeStateException("하행종점역을 찾을 수 없습니다."));
    }

    public boolean isContainStation(final Long stationId) {
        return stationEdges.stream()
                .anyMatch(edge -> edge.isUpStationId(stationId) || edge.isDownStationId(stationId));
    }

    public void removeStation(final Long stationId) {
        validateIsContainStation(stationId);
        final Set<StationEdge> edgesToRemove = stationEdges.stream()
                .filter(edge -> edge.isUpStationId(stationId) || edge.isDownStationId(stationId))
                .collect(Collectors.toSet());

        if (edgesToRemove.size() == 1) {
            stationEdges.removeAll(edgesToRemove);
        }
        if (edgesToRemove.size() == 2) {
            final long downStationId = findDownStationIdOf(edgesToRemove, stationId);
            final long upStationId = findUpStationIdOf(edgesToRemove, stationId);
            final int mergedDistance = edgesToRemove.stream()
                    .mapToInt(StationEdge::getDistance)
                    .sum();
            stationEdges.removeAll(edgesToRemove);
            stationEdges.add(new StationEdge(upStationId, downStationId, mergedDistance));
        }
    }

    private long findDownStationIdOf(final Set<StationEdge> edgePool, final Long upStationId) {
        return edgePool.stream()
                .filter(edge -> edge.isUpStationId(upStationId))
                .mapToLong(StationEdge::getDownStationId)
                .findFirst()
                .orElseThrow(() -> new IllegalStationEdgeStateException(upStationId + "를 상행역으로 가지는 구간이 없습니다."));
    }

    private long findUpStationIdOf(final Set<StationEdge> edgePool, final Long downStationId) {
        return edgePool.stream()
                .filter(edge -> edge.isDownStationId(downStationId))
                .mapToLong(StationEdge::getUpStationId)
                .findFirst()
                .orElseThrow(() -> new IllegalStationEdgeStateException(downStationId + "를 하행역으로 가지는 구간이 없습니다."));
    }

    public boolean contains(final Long station1Id, final Long station2Id) {
        return isContainInUpToDownDirection(station1Id, station2Id) || isContainInDownToUpDirection(station2Id, station1Id);
    }

    private boolean isContainInUpToDownDirection(final Long upStationId, final Long downStationId) {
        return stationEdges.contains(new StationEdge(upStationId, downStationId, 1));
    }

    private boolean isContainInDownToUpDirection(final Long upStationId, final Long downStationId) {
        return stationEdges.contains(new StationEdge(downStationId, upStationId, 1));
    }

    public int getEdgeDistanceBetween(final Long station1Id, final Long station2Id) {
        if (isContainInUpToDownDirection(station1Id, station2Id)) {
            return get(station1Id, station2Id).getDistance();
        }
        if (isContainInDownToUpDirection(station1Id, station2Id)) {
            return get(station2Id, station1Id).getDistance();
        }
        throw new StationEdgeNotFoundException();
    }

    private StationEdge get(final Long upStationId, final Long downStationId) {
        return stationEdges.stream()
                .filter(edge -> edge.isUpStationId(upStationId) && edge.isDownStationId(downStationId))
                .findFirst()
                .orElseThrow(StationEdgeNotFoundException::new);
    }

    public List<Long> getStationIdsInOrder() {
        final List<Long> stationIds = new ArrayList<>();

        Long prevId = findUpEndStationId();
        while (stationIds.size() < stationEdges.size()) {
            stationIds.add(prevId);
            prevId = findDownStationIdOf(stationEdges, prevId);
        }
        stationIds.add(findDownEndStationId());
        return stationIds;
    }

    public int getTotalDistance() {
        return stationEdges.stream()
                .mapToInt(StationEdge::getDistance)
                .sum();
    }

    public Set<StationEdge> toSet() {
        return stationEdges;
    }

    public int size() {
        return stationEdges.size();
    }
}
