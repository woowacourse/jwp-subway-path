package subway.jgraph;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.core.Line;
import subway.domain.core.Section;
import subway.domain.core.Station;
import subway.domain.core.Subway;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.domain.path.SectionEdge;

public class JgraphtPathFinder implements PathFinder {

    private final Subway subway;

    public JgraphtPathFinder(final Subway subway) {
        this.subway = subway;
    }

    @Override
    public Path find(final String start, final String end) {
        final DijkstraShortestPath<String, SectionEdge> shortestPath = initializeShortestPath();
        final GraphPath<String, SectionEdge> path = shortestPath.getPath(start, end);
        return new JgraphtPath(path.getEdgeList());
    }

    private DijkstraShortestPath<String, SectionEdge> initializeShortestPath() {
        final WeightedMultigraph<String, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        addVertex(graph);
        addEdge(graph);
        return new DijkstraShortestPath<>(graph);
    }

    private void addVertex(final WeightedMultigraph<String, SectionEdge> graph) {
        subway.getStations().stream()
                .map(Station::getName)
                .forEach(graph::addVertex);
    }

    private void addEdge(final WeightedMultigraph<String, SectionEdge> graph) {
        for (final Line line : subway.getLines()) {
            addEdgeByLine(graph, line);
        }
    }

    private void addEdgeByLine(final WeightedMultigraph<String, SectionEdge> graph, final Line line) {
        for (final Section section : line.getSections()) {
            final SectionEdge sectionEdge = new JgraphtSectionEdge(section, line.getSurcharge(), line.getId());
            graph.addEdge(section.getStartName(), section.getEndName(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistanceValue());
        }
    }
}
