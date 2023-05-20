package subway.infra;

import static subway.exception.ErrorCode.ROUTE_NOT_EXISTS;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.route.GraphProvider;
import subway.domain.section.Section;
import subway.domain.section.SectionDistance;
import subway.domain.section.SubwayLine;
import subway.domain.station.Station;
import subway.exception.BadRequestException;

@Component
public class JgraphtService implements GraphProvider {

    @Override
    public List<Station> getShortestPath(final List<SubwayLine> sections,
                                         final Station sourceStation,
                                         final Station targetStation) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph = createRouteGraph(sections);
        validateRequestRoute(sourceStation, targetStation, routeGraph);
        return getShortestPath(sourceStation, targetStation, routeGraph);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> createRouteGraph(final List<SubwayLine> sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);
        sections.forEach(section -> {
            addVertex(routeGraph, section.getSortedStations());
            addEdge(routeGraph, section.getSections());
        });
        return routeGraph;
    }

    private void addVertex(final WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph,
                           final List<Station> stations) {
        stations.forEach(routeGraph::addVertex);
    }

    private void addEdge(final WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph,
                         final List<Section> sections) {
        sections.forEach(section -> {
            final Station sourceStation = section.source();
            final Station targetStation = section.target();
            final SectionDistance distance = section.distance();
            final DefaultWeightedEdge sectionEdge = routeGraph.addEdge(sourceStation, targetStation);
            routeGraph.setEdgeWeight(sectionEdge, distance.distance());
        });
    }

    private void validateRequestRoute(final Station sourceStation, final Station targetStation,
                                      final WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph) {
        if (!routeGraph.containsVertex(sourceStation) || !routeGraph.containsVertex(targetStation)) {
            throw new BadRequestException(ROUTE_NOT_EXISTS);
        }
    }

    private List<Station> getShortestPath(final Station sourceStation, final Station targetStation,
                                          final WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph) {
        final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(
            routeGraph);
        return shortestPath.getPath(sourceStation, targetStation).getVertexList();
    }
}
