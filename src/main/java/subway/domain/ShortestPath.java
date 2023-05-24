package subway.domain;

import java.util.List;

public class ShortestPath {

    private final List<Station> routes;
    private final Distance distance;

    public ShortestPath(List<Station> routes, Distance distance) {
        this.routes = routes;
        this.distance = distance;
    }

    public List<Station> getRoutes() {
        return routes;
    }

    public Distance getDistance() {
        return distance;
    }
}
