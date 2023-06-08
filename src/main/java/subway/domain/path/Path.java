package subway.domain.path;

import java.util.List;
import subway.domain.section.SectionEdge;
import subway.domain.station.Station;

public class Path {

    private final List<Station> stations;
    private final List<SectionEdge> sectionEdges;

    public Path(List<Station> stations, List<SectionEdge> sectionEdges) {
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
