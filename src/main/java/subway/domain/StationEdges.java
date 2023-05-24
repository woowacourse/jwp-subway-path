package subway.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import subway.exception.StationAlreadyExistsException;
import subway.exception.StationNotFoundException;

public class StationEdges {
    private final List<StationEdge> stationEdges;

    private StationEdges(List<StationEdge> stationEdges) {
        this.stationEdges = stationEdges;
    }

    public static StationEdges from(List<StationEdge> stationEdges) {
        return new StationEdges(new LinkedList<>(stationEdges));
    }

    public void addStationUpperFrom(
            Long newStationId,
            long adjacentStationId,
            Distance distanceFromAdjacentStation
    ) {
        validateNew(newStationId);

        StationEdge adjacentDownStationEdge = getStationEdge(adjacentStationId);
        Distance newEdgeDistance = adjacentDownStationEdge.getDistance().minus(distanceFromAdjacentStation);

        List<StationEdge> splitEdges = adjacentDownStationEdge.split(newEdgeDistance, newStationId);
        replaceBySplitEdges(adjacentDownStationEdge, splitEdges);
    }

    private void replaceBySplitEdges(StationEdge targetEdge, List<StationEdge> splitEdges) {
        int adjacentDownStationIndex = stationEdges.indexOf(targetEdge);
        stationEdges.remove(adjacentDownStationIndex);
        stationEdges.addAll(adjacentDownStationIndex, splitEdges);
    }

    public void addStationDownFrom(
            Long newStationId,
            Long adjacentStationId,
            Distance newStationDistance
    ) {
        validateNew(newStationId);

        if (isLastStation(adjacentStationId)) {
            StationEdge insertedStationEdge = new StationEdge(newStationId, newStationDistance);
            stationEdges.add(insertedStationEdge);
            return;
        }

        StationEdge adjacentStationEdge = getStationEdge(adjacentStationId);
        StationEdge nextStationEdge = next(adjacentStationEdge);

        List<StationEdge> splitEdges = nextStationEdge.split(newStationDistance, newStationId);
        replaceBySplitEdges(nextStationEdge, splitEdges);
    }

    public StationEdge next(StationEdge stationEdge) {
        final int adjacentDownStationIndex = stationEdges.indexOf(stationEdge) + 1;
        return stationEdges.get(adjacentDownStationIndex);
    }

    private boolean isLastStation(Long stationId) {
        return stationEdges.get(stationEdges.size()-1).getDownStationId() == stationId;
    }

    private void validateNew(Long stationId) {
        if (contains(stationId)) {
            throw new StationAlreadyExistsException();
        }
    }

    public StationEdge deleteStation(long stationId) {
        StationEdge stationEdge = getStationEdge(stationId);

        int edgeIndex = stationEdges.indexOf(stationEdge);
        stationEdges.remove(stationEdge);
        if (edgeIndex == stationEdges.size()) {
            return null;
        }

        StationEdge downStationEdge = stationEdges.get(edgeIndex);
        StationEdge combinedStationEdge = new StationEdge(downStationEdge.getDownStationId(),
                stationEdge.getDistance().plus(downStationEdge.getDistance()));
        stationEdges.set(edgeIndex, combinedStationEdge);
        return combinedStationEdge;
    }

    public boolean canDeleteStation() {
        return stationEdges.size() > 2;
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

    public List<Long> getStationIds() {
        return stationEdges.stream()
                .map(stationEdge -> stationEdge.getDownStationId())
                .collect(Collectors.toList());
    }

    public List<StationEdge> getStationEdges() {
        return new ArrayList<>(stationEdges);
    }
}
