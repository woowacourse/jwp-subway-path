package subway.domain.subway;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.Path;
import subway.domain.line.Line;
import subway.domain.passenger.Passenger;
import subway.domain.station.Station;
import subway.domain.subway.billing_policy.BillingPolices;
import subway.domain.subway.billing_policy.Fare;
import subway.domain.subway.discount_policy.DiscountPolicies;
import subway.domain.subway.route_map.RouteMap;

@Component
public class Subway {

    private final RouteMap routeMap;
    private final BillingPolices billingPolicies;
    private final DiscountPolicies discountPolicies;

    public Subway(final RouteMap routeMap, final BillingPolices billingPolicies,
                  final DiscountPolicies discountPolicies) {
        this.routeMap = routeMap;
        this.billingPolicies = billingPolicies;
        this.discountPolicies = discountPolicies;
    }

    public void updateRouteMap(final List<Line> lines) {
        routeMap.update(lines);
    }

    public Path findShortestPath(final Station source, final Station destination) {
        return routeMap.findShortestPath(source, destination);
    }

    public Fare calculateFare(final Path path, final Passenger passenger) {
        final Fare fare = billingPolicies.calculateFare(path);
        return discountPolicies.calculateDiscountedFare(fare, passenger);
    }
}
