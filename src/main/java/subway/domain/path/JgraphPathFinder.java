package subway.domain.path;

import java.util.Arrays;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.Stations;

public class JgraphPathFinder implements PathFinder {

    private final List<Line> lines;
    private final WeightedMultigraph<Station, Section> graph;

    public JgraphPathFinder(final List<Line> lines) {
        this.lines = lines;
        graph = initGraph();
    }

    private WeightedMultigraph<Station, Section> initGraph() {
        final WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        addVertex(graph);
        setEdgeWeight(graph);
        return graph;
    }

    private void addVertex(final WeightedMultigraph<Station, Section> graph) {
        final Stations stations = getAllDistinctStations();

        for (final Station station : stations.stations()) {
            graph.addVertex(station);
        }
    }

    private Stations getAllDistinctStations() {
        Stations stations = Stations.emptyStations();

        for (final Line line : lines) {
            stations = stations.addStations(line.getStations());
        }

        return stations;
    }

    private void setEdgeWeight(final WeightedMultigraph<Station, Section> graph) {
        for (final Line line : lines) {
            setEachSectionsEdgeWeight(graph, line.getSections());
        }
    }

    private void setEachSectionsEdgeWeight(final WeightedMultigraph<Station, Section> graph, final Sections sections) {
        for (final Section section : sections.sections()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(), section);
            graph.setEdgeWeight(section, section.getDistance().distance());
        }
    }

    @Override
    public Path findPath(final Station departure, final Station arrival) {
        validateContainStation(departure, arrival);

        final DijkstraShortestPath<Station, Section> dijkstraShortestPath = new DijkstraShortestPath(graph);
        final GraphPath<Station, Section> path = dijkstraShortestPath.getPath(departure, arrival);

        validateLinkedStations(path);

        final Stations pathStations = new Stations(path.getVertexList());
        final Sections pathSections = new Sections(path.getEdgeList());

        return new Path(pathStations, pathSections);
    }

    private void validateContainStation(final Station... stations) {
        if (!Arrays.stream(stations).allMatch(graph::containsVertex)) {
            throw new IllegalArgumentException("전체 경로상 없는 역이 있습니다.");
        }
    }

    private void validateLinkedStations(final GraphPath<Station, Section> path) {
        if (path == null) {
            throw new IllegalArgumentException("두 역은 이어져 있지 않습니다.");
        }
    }
}
