package subway.domain.subway;

import subway.domain.Path;
import subway.domain.station.Station;
import subway.domain.subway.billing_policy.BillingPolicy;
import subway.domain.subway.billing_policy.Fare;
import subway.domain.subway.route_map.RouteMap;

public final class Subway {

    private final RouteMap routeMap;
    private final BillingPolicy billingPolicy;

    public Subway(final RouteMap routeMap, final BillingPolicy billingPolicy) {
        this.routeMap = routeMap;
        this.billingPolicy = billingPolicy;
    }

    public Path findShortestPath(Station source, Station destination) {
        return routeMap.findShortestPath(source, destination);
    }

    public Fare calculateFare(Path path) {
        return billingPolicy.calculateFare(path);
    }
}
