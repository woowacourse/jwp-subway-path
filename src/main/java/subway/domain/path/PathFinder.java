package subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.core.Line;
import subway.domain.core.Section;
import subway.domain.core.Station;
import subway.domain.core.Subway;

public class PathFinder {

    private final Subway subway;

    public PathFinder(final Subway subway) {
        this.subway = subway;
    }

    public Path find(final String start, final String end) {
        final DijkstraShortestPath<String, SectionEdge> shortestPath = initializeShortestPath();
        final GraphPath<String, SectionEdge> path = shortestPath.getPath(start, end);
        return new Path(path.getEdgeList());
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
            final SectionEdge sectionEdge = new SectionEdge(section, line.getSurcharge(), line.getId());
            graph.addEdge(section.getStartName(), section.getEndName(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistanceValue());
        }
    }
}
