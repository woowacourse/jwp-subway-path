package subway.line.domain.navigation;

import subway.line.domain.navigation.domain.Path;
import subway.line.domain.navigation.domain.SubwayGraph;
import subway.line.domain.section.Section;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

import java.util.List;

public class Navigation {
    private final Path path;

    public Navigation(List<List<Section>> sectionsOfAllLine, SubwayGraph graph) {
        graph.initialize(sectionsOfAllLine);
        this.path = graph.makePath();
    }

    public List<Station> findShortestPath(Station stationA, Station stationB) {
        return path.findShortestPath(stationA, stationB);
    }

    public Distance findShortestDistance(Station stationA, Station stationB) {
        return path.findShortestDistance(stationA, stationB);
    }
}
