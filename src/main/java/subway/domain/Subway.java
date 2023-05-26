package subway.domain;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import subway.domain.exception.ShortestPathLibraryException;
import subway.domain.exception.ShortestPathSearchFailedException;

public class Subway {

    private final List<Line> lines;

    public Subway(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public Path getShortestPath(Station from, Station to) {
        validateContains(from);
        validateContains(to);
        return handleExceptionOf(() -> findShortestPath(from, to));
    }

    private void validateContains(Station station) {
        lines.stream()
                .filter(line -> line.contains(station))
                .findAny()
                .orElseThrow(() -> new ShortestPathSearchFailedException(
                        "지하철에 포함되지 않은 역(" + station.getName() + ")이 있습니다"
                ));
    }

    private Path findShortestPath(Station from, Station to) {
        WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        for (Line line : lines) {
            addVertexTo(graph, line.getStations());
            addEdgeTo(graph, line.getSections());
        }
        GraphPath<Station, Section> path = DijkstraShortestPath.findPathBetween(graph, from, to);
        validateHasAny(path);

        return new Path(path.getEdgeList());
    }

    private void validateHasAny(GraphPath<Station, Section> path) {
        if (isNull(path)) {
            throw new ShortestPathSearchFailedException("두 역간의 경로가 없습니다");
        }
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
        } catch (IllegalArgumentException exception) {
            throw new ShortestPathLibraryException("jgrapht: " + exception.getMessage());
        }
    }
}
