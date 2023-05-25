package subway.business.service.path;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.business.domain.PathCalculator;
import subway.business.domain.Section;
import subway.business.domain.Station;
import subway.business.domain.Subway;

import java.util.List;

public class PathCalculatorImpl implements PathCalculator {
    private final WeightedGraph<Station, DefaultWeightedEdge> graph;

    public PathCalculatorImpl(WeightedGraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static PathCalculatorImpl from(Subway subway) {
        WeightedGraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        subway.getStations().forEach(graph::addVertex);
        subway.getSections().forEach(section -> setEdgeWeightFromSection(graph, section));
        return new PathCalculatorImpl(graph);
    }

    private static void setEdgeWeightFromSection(WeightedGraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.setEdgeWeight(
                graph.addEdge(section.getUpwardStation(), section.getDownwardStation()),
                section.getDistance());
    }

    @Override
    public int getTotalDistance(Station sourceStation, Station destStation) {
        DijkstraShortestPath path = new DijkstraShortestPath(graph);
        return (int) path.getPathWeight(sourceStation, destStation);
    }

    @Override
    public List<Station> getShortestPath(Station sourceStation, Station destStation) {
        DijkstraShortestPath path = new DijkstraShortestPath(graph);
        try {
            return path.getPath(sourceStation, destStation).getVertexList();
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("시작역에서 도착역까지 이동할 수 있는 경로가 존재하지 않습니다.");
        }
    }
}
