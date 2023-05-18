package subway.domain;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;

public class SubwayMap {

    private final Graph<Station, Section> subwayGraph = new SimpleWeightedGraph<>(Section.class);

    public SubwayMap(final List<Station> stations, final List<Section> sections) {

        for (Station station : stations) {
            subwayGraph.addVertex(station);
        }

        for (Section section : sections) {
            subwayGraph.addEdge(section.getUpper(), section.getLower(), section);
            subwayGraph.setEdgeWeight(section, section.getDistance().getValue());
        }
    }

    public List<Section> getShortestPath(final Station from, final Station to) {
        DijkstraShortestPath<Station, Section> dijkstraShortestPath = new DijkstraShortestPath<>(subwayGraph);
        return dijkstraShortestPath.getPath(from, to).getEdgeList();
    }
}
