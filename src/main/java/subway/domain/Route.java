package subway.domain;

import java.util.List;

public class Route {

    private final RouteFinder routeFinder;

    public Route(final List<Line> lines) {
        this.routeFinder = new JgraphtRouteFinder(lines);
    }

    public List<String> findShortestRoute(final String startStation, final String endStation) {
        return routeFinder.findShortestRoute(startStation, endStation);
    }

    public Distance findShortestRouteDistance(final String startStation, final String endStation) {
        return routeFinder.findShortestRouteDistance(startStation, endStation);
    }

    public List<EdgeSection> findShortestRouteSections(final String startStation, final String endStation) {
        return routeFinder.findShortestRouteSections(startStation, endStation);
    }
}
