package subway.domain;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.GraphDelegator;
import java.util.List;

public class PathFinder {

    private final Graph<Station, StationEdge> subwayRoute = new DefaultUndirectedWeightedGraph<>(StationEdge.class);

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

    public List<Station> findShortestPath(final Station startStation, final Station endStation) {
        DijkstraShortestPath<Station, StationEdge> dijkstraShortestPath = new DijkstraShortestPath<>(subwayRoute);

        return dijkstraShortestPath.getPath(startStation, endStation).getVertexList();
    }

    public double calculateShortestDistance(final Station startStation, final Station endStation) {
        DijkstraShortestPath<Station, StationEdge> dijkstraShortestPath = new DijkstraShortestPath<>(subwayRoute);

        return dijkstraShortestPath.getPathWeight(startStation, endStation);
    }

    public Graph<Station, DefaultEdge> getSubwayRoute() {
        return new GraphDelegator<>(subwayRoute);
    }
}
