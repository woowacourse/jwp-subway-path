package subway.service.path.dto;

import subway.service.path.domain.SectionEdge;
import subway.service.station.domain.Station;

import java.util.List;

public class ShortestPath {
    private final List<Station> stations;
    private final List<SectionEdge> edges;

    public ShortestPath(List<Station> stations, List<SectionEdge> edges) {
        this.stations = stations;
        this.edges = edges;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<SectionEdge> getEdges() {
        return edges;
    }
}
