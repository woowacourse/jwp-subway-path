package subway.domain;

import java.util.List;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static PathFinder from(Sections sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        initializeWithSections(sections.getSections(), graph);
        return new PathFinder(graph);
    }

    private static void initializeWithSections(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            addSectionValue(section, graph);
        }
    }

    private static void addSectionValue(Section section, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        Station source = section.getStartStation();
        Station dest = section.getEndStation();
        graph.addVertex(source);
        graph.addVertex(dest);
        graph.setEdgeWeight(graph.addEdge(source, dest), section.getDistance());
    }

}
