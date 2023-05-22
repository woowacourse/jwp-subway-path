package subway.domain.subway;

import java.util.List;
import subway.domain.vo.Distance;
import subway.domain.vo.Charge;

public class Path {
    private final List<Route> routes;
    private final Distance totalDistance;
    private final Charge totalCharge;

    public Path(List<Route> routes, Distance totalDistance, Charge totalCharge) {
        this.routes = routes;
        this.totalDistance = totalDistance;
        this.totalCharge = totalCharge;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public Distance getTotalDistance() {
        return totalDistance;
    }

    public Charge getTotalCharge() {
        return totalCharge;
    }
}
