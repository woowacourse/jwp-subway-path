package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import subway.application.exception.SubwayServiceException;

import java.util.Set;

public class SubwayStructure {

    private static final String INVALID_NOT_FOUND_STATION_MESSAGE = "역이 노선에 없습니다.";

    private final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> structure;

    public SubwayStructure(SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> structure) {
        this.structure = structure;
    }

    public Station findStartStation() {
        return structure.vertexSet()
                .stream()
                .filter(this::isStartStation)
                .findFirst()
                .orElse(null);
    }

    private boolean isStartStation(final Station station) {
        return structure.incomingEdgesOf(station).isEmpty();
    }

    public boolean hasLeftSection(final Station station) {
        validateStation(station);
        return !isStartStation(station);
    }

    private void validateStation(Station station) {
        if (hasNoStation(station)) {
            throw new SubwayServiceException(INVALID_NOT_FOUND_STATION_MESSAGE);
        }
    }

    public boolean hasRightSection(final Station station) {
        validateStation(station);
        return !isEndStation(station);
    }

    public boolean isEndStation(final Station station) {
        return structure.outgoingEdgesOf(station).isEmpty();
    }

    public boolean hasStation(final Station station) {
        return structure.containsVertex(station);
    }

    public boolean hasNoStation(final Station station) {
        return !hasStation(station);
    }

    public boolean isStationsEmpty() {
        return structure.edgeSet().isEmpty();
    }

    public boolean isStationsPresent() {
        return !isStationsEmpty();
    }

    public Set<DefaultWeightedEdge> getRightEdge(Station station) {
        return structure.outgoingEdgesOf(station);
    }

    public Set<DefaultWeightedEdge> getLeftEdge(Station station) {
        return structure.incomingEdgesOf(station);
    }

    public Station getRightStationByEdge(DefaultWeightedEdge edge) {
        return structure.getEdgeTarget(edge);
    }

    public Station getLeftStationByEdge(DefaultWeightedEdge edge) {
        return structure.getEdgeSource(edge);
    }

    public int getDistance(DefaultWeightedEdge edge) {
        return (int) structure.getEdgeWeight(edge);
    }
}
