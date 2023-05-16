package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Path {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private Path(final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static Path from(final List<Line> lines){
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        List<Station> stations = extractStations(lines);
        List<Section> sections = extractSections(lines);
        for(Station station : stations){
            graph.addVertex(station);
        }
        for(Section section : sections){
            Station upwardStation = section.getUpwardStation();
            Station downwardStation = section.getDownwardStation();
            graph.setEdgeWeight(graph.addEdge(upwardStation, downwardStation), section.getDistance());
        }
        return new Path(graph);
    }

    private static List<Station> extractStations(final List<Line> lines){
        List<Station> stations = new ArrayList<>();
        for(Line line : lines){
            stations.addAll(line.getStations());
        }
        return removeDuplicatedStation(stations);
    }

    private static List<Station> removeDuplicatedStation(final List<Station> stations) {
        return stations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private static List<Section> extractSections(final List<Line> lines){
        List<Section> sections = new ArrayList<>();
        List<Section> emptySections = new ArrayList<>();
        for(Line line : lines){
            sections.addAll(line.getSections());
            emptySections.add(line.getUpwardEndSection());
            emptySections.add(line.getDownwardEndSection());
        }
        sections.removeAll(emptySections);
        return sections;
    }

    public List<Station> getShortestPathStations(final Station from, final Station to) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(from, to).getVertexList();
    }

    public int getShortestPathDistance(final Station from, final Station to) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return (int) dijkstraShortestPath.getPath(from, to).getWeight();
    }
}
