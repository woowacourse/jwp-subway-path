package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Path {

    private final WeightedMultigraph<Station, SectionEdge> graph;

    private Path(final WeightedMultigraph<Station, SectionEdge> graph) {
        this.graph = graph;
    }

    public static Path from(final List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        List<Station> stations = extractStations(lines);
        List<Section> sections = extractSections(lines);
        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : sections) {
            Station upwardStation = section.getUpwardStation();
            Station downwardStation = section.getDownwardStation();
            graph.setEdgeWeight(graph.addEdge(upwardStation, downwardStation), section.getDistance());
        }
        return new Path(graph);
    }

    private static List<Station> extractStations(final List<Line> lines) {
        List<Station> stations = new ArrayList<>();
        for (Line line : lines) {
            stations.addAll(line.getStations());
        }
        return removeDuplicatedStation(stations);
    }

    private static List<Station> removeDuplicatedStation(final List<Station> stations) {
        return stations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private static List<Section> extractSections(final List<Line> lines) {
        List<Section> sections = new ArrayList<>();
        for (Line line : lines) {
            sections.addAll(line.getSectionsExceptEmpty());
        }
        return sections;
    }

    public List<Station> getShortestPathStations(final Station from, final Station to) {
        validateSameStations(from, to);
        validateUnconnectedStations(from, to);
        return findPath(from, to).getVertexList();
    }

    private GraphPath<Station, SectionEdge> findPath(final Station from, final Station to) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(from, to);
        if (path == null) {
            throw new IllegalArgumentException("[ERROR] 출발역과 도착역이 연결되어 있지 않습니다.");
        }
        return path;
    }

    private void validateUnconnectedStations(final Station from, final Station to) {
        if (!graph.containsVertex(from) || !graph.containsVertex(to)) {
            throw new IllegalArgumentException("[ERROR] 노선에 등록되지 않은 역이 존재합니다.");
        }
    }

    private static void validateSameStations(final Station from, final Station to) {
        if (from.equals(to)) {
            throw new IllegalArgumentException("[ERROR] 출발역과 도착역이 같아 조회할 경로가 없습니다.");
        }
    }

    public List<Section> getShortestPathSections(final Station from, final Station to) {
        validateSameStations(from, to);
        validateUnconnectedStations(from, to);
        return findPath(from, to).getEdgeList()
                .stream()
                .map(SectionEdge::toSection)
                .collect(Collectors.toUnmodifiableList());
    }

    public int getShortestPathDistance(final Station from, final Station to) {
        validateSameStations(from, to);
        validateUnconnectedStations(from, to);
        return (int) findPath(from, to).getWeight();
    }
}
