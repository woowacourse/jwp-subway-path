package subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.section.domain.Section;
import subway.station.domain.Station;

import java.util.List;

public class Path {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public Path(final List<Station> stations, final List<Section> sections) {
        addVertex(stations);
        addEdge(sections);
    }

    private void addVertex(final List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdge(final List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(
                    graph.addEdge(
                            section.getUpStation(),
                            section.getDownStation()
                    ),
                    section.getDistanceValue()
            );
        }
    }

    public List<Station> findPath(final Station startStation, final Station endStation) {
        return new DijkstraShortestPath(graph).getPath(startStation, endStation).getVertexList();
    }

    public double findPathDistance(final Station startStation, final Station endStation) {
        return new DijkstraShortestPath(graph).getPath(startStation, endStation).getWeight();
    }

}
