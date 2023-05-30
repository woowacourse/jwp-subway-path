package subway.domain;

import static java.util.stream.Collectors.toList;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.GraphDelegator;
import java.util.List;

public class PathFinder {

    private final Graph<Station, StationEdge> subwayRoute = new DefaultUndirectedWeightedGraph<>(StationEdge.class);
    private final ShortestPathAlgorithm<Station, StationEdge> shortestPathAlgorithm = new DijkstraShortestPath<>(subwayRoute);

    public PathFinder(final List<Section> sections) {
        for (final Section section : sections) {
            addVertexAndEdge(section);
        }
    }

    private void addVertexAndEdge(final Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        Distance distance = section.getDistance();

        subwayRoute.addVertex(upStation);
        subwayRoute.addVertex(downStation);
        StationEdge stationEdge = new StationEdge(section.getDistance(), section.getLine());
        subwayRoute.addEdge(upStation, downStation, stationEdge);
        subwayRoute.setEdgeWeight(upStation, downStation, distance.getValue());
    }

    public Path findShortesPath(final Station startStation, final Station endStation) {
        return new Path(
                findPassStations(startStation, endStation),
                calculateShortestDistance(startStation, endStation),
                findPassLine(startStation, endStation)
        );
    }

    private List<Station> findPassStations(final Station startStation, final Station endStation) {
        return shortestPathAlgorithm.getPath(startStation, endStation).getVertexList();
    }

    private Distance calculateShortestDistance(final Station startStation, final Station endStation) {
        double value = shortestPathAlgorithm.getPathWeight(startStation, endStation);

        return new Distance(value);
    }

    private List<Line> findPassLine(final Station startStation, final Station endStation) {
        List<StationEdge> edges = shortestPathAlgorithm.getPath(startStation, endStation).getEdgeList();

        return edges.stream()
                .map(StationEdge::getLine)
                .distinct()
                .collect(toList());
    }

    public Graph<Station, StationEdge> getSubwayRoute() {
        return new GraphDelegator<>(subwayRoute);
    }
}
