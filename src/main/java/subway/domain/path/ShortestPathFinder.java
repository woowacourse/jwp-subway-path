package subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Stations;
import subway.exception.IllegalDestinationException;
import subway.exception.StationsNotConnectedException;

import java.util.Objects;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class ShortestPathFinder {

    public ShortestPath findShortestPath(Sections sections, Stations stations, Station departure, Station destination) {
        validateSameStations(departure, destination);
        DijkstraShortestPath<Station, Section> dijkstraShortestPath = createDijkstraPath(stations, sections);
        GraphPath<Station, Section> path = getPath(dijkstraShortestPath, departure, destination);
        int shortestPathDistance = (int) dijkstraShortestPath.getPathWeight(departure, destination);
        return path.getVertexList()
                .stream()
                .collect(collectingAndThen(toList(), shortestPathStations -> new ShortestPath(shortestPathStations, shortestPathDistance)));
    }

    private void validateSameStations(Station departure, Station destination) {
        if (departure.equals(destination)) {
            throw new IllegalDestinationException("출발역과 동일한 도착역을 입력할 수 없습니다.");
        }
    }

    private DijkstraShortestPath<Station, Section> createDijkstraPath(Stations stations, Sections sections) {
        WeightedMultigraph graph = new WeightedMultigraph(Section.class);
        graph = stations.addStationsToGraph(graph);
        graph = sections.setEdgeWeightToGraph(graph);
        return new DijkstraShortestPath(graph);
    }

    private GraphPath<Station, Section> getPath(DijkstraShortestPath<Station, Section> dijkstraShortestPath, Station departure, Station destination) {
        GraphPath<Station, Section> path = dijkstraShortestPath.getPath(departure, destination);
        if (Objects.isNull(path)) {
            throw new StationsNotConnectedException("찾는 역은 연결되어 있지 않습니다.");
        }
        return path;
    }
}
