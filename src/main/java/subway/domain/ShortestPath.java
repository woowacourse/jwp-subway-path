package subway.domain;

import java.util.List;

public class ShortestPath {

    private final List<Station> stations;
    private final List<SectionEdge> sectionEdges;

    public ShortestPath(List<Station> stations, List<SectionEdge> sectionEdges) {
        this.stations = stations;
        this.sectionEdges = sectionEdges;
    }

    public Long getDistance() {
        return sectionEdges.stream()
                .mapToLong(sectionEdge -> sectionEdge.getSection().getDistance())
                .sum();
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }
}
