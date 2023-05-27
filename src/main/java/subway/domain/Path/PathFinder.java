package subway.domain.Path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.line.Lines;
import subway.domain.policy.basic.FarePolicy;
import subway.domain.section.Section;
import subway.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {

    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;
    private final FarePolicy farePolicy;

    public PathFinder(DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath, FarePolicy farePolicy) {
        this.dijkstraShortestPath = dijkstraShortestPath;
        this.farePolicy = farePolicy;
    }

    public static PathFinder from(List<Section> allSections, FarePolicy farePolicy) {
        WeightedMultigraph<Station, SectionEdge> graph =
                new WeightedMultigraph<>(SectionEdge.class);

        if (allSections.size() == 0) {
            throw new IllegalArgumentException("[ERROR] 경로를 찾을 구간들이 존재하지 않습니다.");
        }

        for (Section section : allSections) {
            graph.addVertex(section.getUpward());
            graph.addVertex(section.getDownward());
            graph.addEdge(section.getUpward(), section.getDownward(), new SectionEdge(section.getLine()));

            SectionEdge edge = graph.getEdge(section.getUpward(), section.getDownward());
            graph.setEdgeWeight(edge, section.getDistance());
        }

        return new PathFinder(new DijkstraShortestPath<>(graph), farePolicy);
    }

    public List<Station> findShortestPath(Station source, Station destination) {
        validateStations(source, destination);

        return dijkstraShortestPath.getPath(source, destination).getVertexList();
    }

    private static void validateStations(Station source, Station destination) {
        if (source.equals(destination)) {
            throw new IllegalArgumentException("[ERROR] 동일한 역 간 경로 조회는 불가능합니다.");
        }
    }

    public int findShortestDistance(Station source, Station destination) {
        validateStations(source, destination);
        return (int) dijkstraShortestPath.getPathWeight(source, destination);
    }


    public Fare calculateFare(Station source, Station destination) {
        List<SectionEdge> sectionEdges = dijkstraShortestPath
                .getPath(source, destination)
                .getEdgeList();

        List<Line> involvedLines = sectionEdges.stream()
                .map(SectionEdge::getLine)
                .collect(Collectors.toList());

        int shortestDistance = findShortestDistance(source, destination);

        return Fare.from(farePolicy.calculateFare(shortestDistance, Lines.from(involvedLines)));
    }
}
