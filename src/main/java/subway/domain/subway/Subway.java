package subway.domain.subway;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.Path;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.domain.subway.billing_policy.BillingPolices;
import subway.domain.subway.billing_policy.Fare;
import subway.domain.subway.route_map.RouteMap;

@Component
public final class Subway {

    private final RouteMap routeMap;
    private final BillingPolices billingPolicies;

    public Subway(final RouteMap routeMap, final BillingPolices billingPolicies) {
        this.routeMap = routeMap;
        this.billingPolicies = billingPolicies;
    }

    public void updateRouteMap(final List<Line> lines) {
        routeMap.update(lines);
    }

    public Path findShortestPath(Station source, Station destination) {
        return routeMap.findShortestPath(source, destination);
    }

    public Fare calculateFare(Path path) {
        return billingPolicies.calculateFare(path);
    }
}
