package subway.domain.path.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.section.Sections;
import subway.domain.station.Station;

public class PathGraph extends WeightedMultigraph<Station, PathEdge> {

    private PathGraph(final Class<? extends PathEdge> edgeClass) {
        super(edgeClass);
    }

    public static PathGraph from(List<Line> lines) {
        final PathGraph pathGraph = new PathGraph(PathEdge.class);

        for (Line line : lines) {
            pathGraph.addSectionPath(line);
        }

        return pathGraph;
    }

    private void addSectionPath(final Line line) {
        final Sections sections = line.getSections();
        final List<Station> sectionStations = sections.findStationsByOrdered();

        for (Station station : sectionStations) {
            addPath(line, sections.findAllAdjustStationByStation(station), station);
        }
    }

    private void addPath(final Line line, final List<Station> adjustStations, final Station station) {
        addVertex(station);

        for (Station adjustStation : adjustStations) {
            addVertex(adjustStation);

            final PathEdge pathEdge = PathEdge.of(station, adjustStation, line);

            addEdge(station, adjustStation, pathEdge);
            setEdgeWeight(pathEdge, pathEdge.getWeight());
        }
    }

    public List<PathEdges> findShortestPathSections(final Station sourceStation, final Station targetStation) {
        final List<PathEdges> result = new ArrayList<>();
        final Queue<PathEdge> shortestPathEdges = calculateShortestPathEdge(sourceStation, targetStation);

        while (!shortestPathEdges.isEmpty()) {
            final PathEdges pathEdges = calculateSameLinePathEdges(shortestPathEdges);

            result.add(pathEdges);
        }

        return result;
    }

    public PathEdges calculateSameLinePathEdges(final Queue<PathEdge> shortestPathEdges) {
        final PathEdges pathEdges = PathEdges.create();

        while (!shortestPathEdges.isEmpty() && pathEdges.isSameLine(shortestPathEdges.peek())) {
            pathEdges.add(shortestPathEdges.poll());
        }

        return pathEdges;
    }

    private Queue<PathEdge> calculateShortestPathEdge(final Station sourceStation, final Station targetStation) {
        final DijkstraShortestPath<Station, PathEdge> dijkstraShortestPath = new DijkstraShortestPath<>(this);
        final GraphPath<Station, PathEdge> graphPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        final List<PathEdge> shortestPathEdges = graphPath.getEdgeList();

        return new LinkedList<>(shortestPathEdges);
    }
}
