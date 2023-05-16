package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import subway.exception.StationAlreadyExistsException;
import subway.exception.StationNotFoundException;

public class StationEdges {
    private final List<StationEdge> stationEdges;

    public StationEdges(List<StationEdge> stationEdges) {
        this.stationEdges = stationEdges;
    }

    public void addStationUpperFrom(
            Long newStationId,
            long adjacentStationId,
            Distance distanceFromAdjacentStation
    ) {
        validateNew(newStationId);

        StationEdge adjacentDownStationEdge = getStationEdge(adjacentStationId);
        int adjacentDownStationIndex = stationEdges.indexOf(adjacentDownStationEdge);

        Distance newEdgeDistance = adjacentDownStationEdge.getDistance().minus(distanceFromAdjacentStation);
        List<StationEdge> splitEdges = adjacentDownStationEdge.split(newEdgeDistance, newStationId);
        stationEdges.remove(adjacentDownStationIndex);
        stationEdges.addAll(adjacentDownStationIndex, splitEdges);
    }

    public void addStationDownFrom(
            Long newStationId,
            Long adjacentStationId,
            Distance newStationDistance
    ) {
        validateNew(newStationId);

        final int adjacentDownStationIndex = stationEdges.indexOf(getStationEdge(adjacentStationId)) + 1;
        boolean isLastEdge = adjacentDownStationIndex == stationEdges.size();
        if (isLastEdge) {
            StationEdge insertedStationEdge = new StationEdge(newStationId, newStationDistance);
            stationEdges.add(insertedStationEdge);
            return;
        }

        StationEdge adjacentDownStationEdge = stationEdges.get(adjacentDownStationIndex);
        List<StationEdge> splitEdges = adjacentDownStationEdge.split(newStationDistance, newStationId);
        stationEdges.remove(adjacentDownStationIndex);
        stationEdges.addAll(adjacentDownStationIndex, splitEdges);
    }

    private void validateNew(Long stationId) {
        if (contains(stationId)) {
            throw new StationAlreadyExistsException();
        }
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
