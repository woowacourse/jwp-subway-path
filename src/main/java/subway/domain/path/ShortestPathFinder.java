package subway.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Stations;

import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class ShortestPathFinder {

    public ShortestPath findShortestPath(Sections sections, Stations stations, Station departure, Station destination) {
        WeightedMultigraph<Station, Section> graph = new WeightedMultigraph(Section.class);

        WeightedMultigraph<Station, Section> graphWithVertex = stations.addStationsToGraph(graph);
        WeightedMultigraph<Station, Section> graphWithEdge = sections.setEdgeWeightToGraph(graphWithVertex);

        DijkstraShortestPath<Station, Section> dijkstraShortestPath = new DijkstraShortestPath(graphWithEdge);

        List<Station> shortestPath = dijkstraShortestPath.getPath(departure, destination).getVertexList();
        int shortestPathDistance = (int) dijkstraShortestPath.getPathWeight(departure, destination);

        return shortestPath.stream()
                .collect(collectingAndThen(toList(), shortestPathStations -> new ShortestPath(shortestPathStations, shortestPathDistance)));
    }
}
