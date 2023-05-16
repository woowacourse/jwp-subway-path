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
            Long insertStationId,
            long adjacentStationId,
            Distance distance
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
            Distance distance
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

        Distance distanceFromDown = adjacentDownStationEdge.getDistance().minus(distance);

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
