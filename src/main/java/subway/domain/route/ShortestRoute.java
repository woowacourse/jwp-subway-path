package subway.domain.route;

import java.util.List;
import java.util.stream.IntStream;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.section.Section;
import subway.domain.section.SectionDistance;
import subway.domain.section.Sections;
import subway.domain.station.Station;

public class ShortestRoute {

    private final List<Station> shortestRoutes;

    public ShortestRoute(final RouteGraph routeGraph, final Station sourceStation, final Station targetStation) {
        final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(
            routeGraph.getRouteGraph());
        this.shortestRoutes = shortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    public Distance getShortestDistance(final List<Section> allSections) {
        return IntStream.range(0, shortestRoutes.size() - 1)
            .mapToObj(i -> new Section(shortestRoutes.get(i), shortestRoutes.get(i + 1), SectionDistance.zero()))
            .flatMap(section -> allSections.stream().filter(section::isSameSection))
            .map(section -> new Distance(section.getDistance().distance()))
            .reduce(new Distance(0), Distance::add);
    }

    public List<Station> getShortestRoutes() {
        return shortestRoutes;
    }
}
