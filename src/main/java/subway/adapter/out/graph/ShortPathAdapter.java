package subway.adapter.out.graph;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.adapter.out.graph.dto.RouteDto;
import subway.application.port.out.graph.ShortPathPort;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ShortPathAdapter implements ShortPathPort {

    @Override
    public RouteDto findSortPath(final Station fromStation, final Station toStation, final Map<Long, Sections> sectionsByLine) {
        WeightedMultigraph<Station, LineWeightedEdge> graph
                = new WeightedMultigraph<>(LineWeightedEdge.class);
        for (Long lineId : sectionsByLine.keySet()) {
            sectionsByLine.get(lineId).getSortedStations()
                    .forEach(graph::addVertex);

            sectionsByLine.get(lineId).getSections().forEach(section -> {
                LineWeightedEdge edge = new LineWeightedEdge(section.getLineId());
                graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
                graph.setEdgeWeight(edge, section.getDistance());
            });
        }

        DijkstraShortestPath<Station, LineWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);

        List<Station> shortestPath;
        int shortestDistance;
        Set<Long> lineIds;
        try {
            GraphPath path = dijkstraShortestPath.getPath(fromStation, toStation);
            shortestPath = path.getVertexList();
            List<LineWeightedEdge> edgeList = path.getEdgeList();
            lineIds = edgeList.stream()
                    .map(edge -> edge.getLineId())
                    .collect(Collectors.toSet());

            shortestDistance = (int) dijkstraShortestPath.getPath(fromStation, toStation).getWeight();

        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("입력된 역이 없습니다.");
        }

        return new RouteDto(shortestPath, shortestDistance, lineIds);
    }
}
