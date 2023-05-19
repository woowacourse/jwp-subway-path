package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubwayGraph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private SubwayGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static SubwayGraph from(Subway subway) {
        Map<Station, List<Section>> subwayMap = subway.getSubwayMap();
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        Set<Station> stations = subwayMap.keySet();
        for (Station station : stations) {
            graph.addVertex(station);
        }

        Collection<List<Section>> values = subwayMap.values();
        for (List<Section> sections : values) {
            for (Section section : sections) {
                graph.setEdgeWeight(graph.addEdge(section.getPreStation(), section.getStation()), section.getDistance());
                graph.setEdgeWeight(graph.addEdge(section.getStation(), section.getPreStation()), section.getDistance());
            }
        }
        return new SubwayGraph(graph);
    }

    public ShortestPath getDijkstraShortestPath(Station departure, Station arrival) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(departure, arrival);
        if (path == null) {
            throw new IllegalArgumentException("최단 경로를 찾을 수 없습니다");
        }
        return ShortestPath.of(path.getVertexList(), path.getWeight());
    }
}
