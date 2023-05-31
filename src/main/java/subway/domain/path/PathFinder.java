package subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import java.util.Set;

public class PathFinder {

    private PathFinder() {
    }

    public static Path findPath(Sections sections, Station startStation, Station endStation) {
        WeightedMultigraph<String, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        Set<String> containingStations = sections.getContainingStationNames();

        addVertex(graph, containingStations);
        addEdgeAndSetEdgeWeight(graph, sections);

        GraphPath<String, SectionEdge> path =
                new DijkstraShortestPath<>(graph)
                        .getPath(startStation.getName(), endStation.getName());
        return new Path(path.getVertexList(), path.getEdgeList(), (int) path.getWeight());
    }

    private static void addVertex(WeightedMultigraph<String, SectionEdge> graph, Set<String> containingStations) {
        for (String stationName : containingStations) {
            graph.addVertex(stationName);
        }
    }

    private static void addEdgeAndSetEdgeWeight(WeightedMultigraph<String, SectionEdge> graph, Sections sections) {
        for (Section section : sections.getSections()) {
            SectionEdge sectionEdge = new SectionEdge(section);
            graph.addEdge(section.getUpStationName(), section.getDownStationName(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistanceValue());
        }
    }
}
