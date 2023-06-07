package subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.subway.Distance;
import subway.domain.subway.Section;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class ShortestDistancePathStrategy implements PathStrategy {
    @Override
    public Map.Entry<List<Long>, Distance> getPathAndDistance(List<Section> allSections, Long startStationId, Long targetStationId) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        setGraph(allSections, graph);
        GraphPath<Long, DefaultWeightedEdge> path = getPath(startStationId, targetStationId, graph);

        return new AbstractMap.SimpleEntry<>(path.getVertexList(), new Distance((int) path.getWeight()));
    }

    private void setGraph(List<Section> allSections, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Section section : allSections) {
            graph.addVertex(section.getUpStationId());
            graph.addVertex(section.getDownStationId());

            graph.setEdgeWeight(graph.addEdge(
                            section.getUpStationId(),
                            section.getDownStationId()),
                    section.getDistance());
        }
    }

    private GraphPath<Long, DefaultWeightedEdge> getPath(Long startStationId, Long targetStationId, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(startStationId, targetStationId);
    }
}
