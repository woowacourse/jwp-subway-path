package subway.domain;

import java.util.List;
import subway.domain.charge.Charge;

public class Path {
    private final List<Route> routes;
    private final double totalDistance;
    private final Charge totalCharge;

    public Path(List<Route> routes, double totalDistance, Charge totalCharge) {
        this.routes = routes;
        this.totalDistance = totalDistance;
        this.totalCharge = totalCharge;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public Charge getTotalCharge() {
        return totalCharge;
    }
}
