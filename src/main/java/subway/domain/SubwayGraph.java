package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SubwayGraph {
    private final WeightedMultigraph<Station, SubwayPathWeightedEdge> graph;

    private SubwayGraph(WeightedMultigraph<Station, SubwayPathWeightedEdge> graph) {
        this.graph = graph;
    }

    public static SubwayGraph from(Subway subway) {
        Map<Station, List<Section>> subwayMap = subway.getSubwayMap();

        WeightedMultigraph<Station, SubwayPathWeightedEdge> graph = getSubwayGraph(subwayMap);
        return new SubwayGraph(graph);

    }

    private static WeightedMultigraph<Station, SubwayPathWeightedEdge> getSubwayGraph(Map<Station, List<Section>> subwayMap) {
        WeightedMultigraph<Station, SubwayPathWeightedEdge> graph = new WeightedMultigraph(SubwayPathWeightedEdge.class);

        Set<Station> stations = subwayMap.keySet();
        for (Station station : stations) {
            graph.addVertex(station);
        }

        Collection<List<Section>> values = subwayMap.values();
        for (List<Section> sections : values) {
            for (Section section : sections) {
                SubwayPathWeightedEdge subwayPathWeightedEdge = new SubwayPathWeightedEdge(section.getLine());
                graph.addEdge(section.getPreStation(), section.getStation(), subwayPathWeightedEdge);
                graph.setEdgeWeight(subwayPathWeightedEdge, section.getDistance().getDistance());
            }
        }

        return graph;
    }

    public ShortestPath getDijkstraShortestPath(Station departure, Station arrival) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(departure, arrival);

        if (path == null) {
            throw new IllegalArgumentException("최단 경로를 찾을 수 없습니다");
        }
        
        List<SubwayPathWeightedEdge> edgeList = path.getEdgeList();
        List<Section> pathSections = edgeList.stream()
                .map(edge -> new Section(edge.getLine(), graph.getEdgeSource(edge),
                        graph.getEdgeTarget(edge), new Distance(graph.getEdgeWeight(edge))))
                .collect(Collectors.toList());
        return ShortestPath.of(pathSections, new Distance(path.getWeight()));
    }
}
