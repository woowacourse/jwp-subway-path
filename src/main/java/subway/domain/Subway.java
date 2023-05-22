package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import subway.domain.exception.ShortestPathSearchFailedException;

public class Subway {

    private final List<Line> lines;

    public Subway(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public Path getShortestPath(Station from, Station to) {
        return handleExceptionOf(() -> findShortestPath(from, to));
    }

    private Path findShortestPath(Station from, Station to) {
        WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        for (Line line : lines) {
            addVertexTo(graph, line.getStations());
            addEdgeTo(graph, line.getSections());
        }
        List<Section> edges = DijkstraShortestPath.findPathBetween(graph, from, to).getEdgeList();
        return new Path(edges);
    }

    private void addVertexTo(WeightedMultigraph<Station, Section> graph, List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdgeTo(WeightedMultigraph<Station, Section> graph, List<Section> sections) {
        for (Section section : sections) {
            graph.addEdge(section.getUpperStation(), section.getLowerStation(), section);
            graph.setEdgeWeight(section, section.getDistance().getValue());
        }
    }

    private Path handleExceptionOf(Supplier<Path> shortestPathSupplier) {
        try {
            return shortestPathSupplier.get();
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            throw new ShortestPathSearchFailedException();
        }
    }
}
