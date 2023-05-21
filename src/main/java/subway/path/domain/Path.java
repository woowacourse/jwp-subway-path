package subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.section.domain.Section;
import subway.station.domain.Station;

import java.util.List;

public class Path {

    private WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public Path(List<Station> stations, List<Section> sections) {
        addVertex(stations);
        addEdge(sections);
    }

    private void addVertex(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station.getNameValue());
        }
    }

    private void addEdge(List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(
                    graph.addEdge(
                            section.getUpStation().getNameValue(),
                            section.getDownStation().getNameValue()
                    ),
                    section.getDistanceValue()
            );
        }
    }

    public List<String> findPath(final String start, final String end) {
        return new DijkstraShortestPath(graph).getPath(start, end).getVertexList();
    }

    public double findPathDistance(final String start, final String end) {
        return new DijkstraShortestPath(graph).getPath(start, end).getWeight();
    }

}
