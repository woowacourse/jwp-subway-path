package subway.domain.route;

import static subway.exception.ErrorCode.ROUTE_NOT_EXISTS;

import java.util.List;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.section.Section;
import subway.domain.section.SectionDistance;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.exception.BadRequestException;

public class RouteGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph;

    public RouteGraph() {
        this.routeGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public void addPossibleRoute(final Sections sections) {
        addVertex(sections.getSortedStations());
        addEdge(sections.getSections());
    }

    public void validateRequestRoute(final Station sourceStation, final Station targetStation) {
        if (!routeGraph.containsVertex(sourceStation) || !routeGraph.containsVertex(targetStation)) {
            throw new BadRequestException(ROUTE_NOT_EXISTS);
        }
    }

    private void addVertex(final List<Station> stations) {
        stations.forEach(routeGraph::addVertex);
    }

    private void addEdge(final List<Section> sections) {
        sections.forEach(section -> {
            final Station sourceStation = section.getSource();
            final Station targetStation = section.getTarget();
            final SectionDistance distance = section.getDistance();
            final DefaultWeightedEdge sectionEdge = routeGraph.addEdge(sourceStation, targetStation);
            routeGraph.setEdgeWeight(sectionEdge, distance.distance());
        });
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> getRouteGraph() {
        return routeGraph;
    }
}
