package subway.domain;

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
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(from, to).getVertexList();
    }

    public List<Section> getShortestPathSections(final Station from, final Station to) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(from, to)
                .getEdgeList()
                .stream()
                .map(edge -> Section.of(edge.getUpwardStation(), edge.getDownwardStation(), edge.getDistance()))
                .collect(Collectors.toUnmodifiableList());
    }

    public int getShortestPathDistance(final Station from, final Station to) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return (int) dijkstraShortestPath.getPath(from, to).getWeight();
    }
}
