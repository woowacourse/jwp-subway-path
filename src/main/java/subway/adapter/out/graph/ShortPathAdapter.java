package subway.adapter.out.graph;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.application.port.out.graph.ShortPathPort;
import subway.domain.Route;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;

public class ShortPathAdapter implements ShortPathPort {

    @Override
    public Route findSortPath(final Station fromStation, final Station toStation, final List<Sections> allSections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Sections sections : allSections) {
            sections.getSortedStations()
                    .forEach(graph::addVertex);

            sections.getSections()
                    .forEach(section -> graph.setEdgeWeight(
                            graph.addEdge(
                                    section.getUpStation(),
                                    section.getDownStation()),
                            section.getDistance())
                    );
        }

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);

        List<Station> shortestPath;
        long shortestDistance;
        try {
            shortestPath = dijkstraShortestPath.getPath(fromStation, toStation).getVertexList();
            shortestDistance = (long) dijkstraShortestPath.getPath(fromStation, toStation).getWeight();
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("입력된 역이 없습니다.");
        }

        return new Route(shortestPath, shortestDistance);
    }
}
