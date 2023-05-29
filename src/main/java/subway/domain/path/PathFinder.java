package subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import java.util.Set;

public class PathFinder {

    private PathFinder() {
    }

    public static Path findPath(Sections sections, Station startStation, Station endStation) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        Set<String> containingStations = sections.getContainingStationNames();

        addVertex(graph, containingStations);
        addEdgeAndSetEdgeWeight(graph, sections);

        GraphPath<String, DefaultWeightedEdge> path =
                new DijkstraShortestPath<>(graph)
                        .getPath(startStation.getName(), endStation.getName());
        return new Path(path.getVertexList(), (int) path.getWeight());
    }

    private static void addVertex(WeightedMultigraph<String, DefaultWeightedEdge> graph, Set<String> containingStations) {
        for (String stationName : containingStations) {
            graph.addVertex(stationName);
        }
    }

    private static void addEdgeAndSetEdgeWeight(WeightedMultigraph<String, DefaultWeightedEdge> graph, Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStationName(), section.getDownStationName()),
                    section.getDistanceValue()
            );
        }
    }
}
