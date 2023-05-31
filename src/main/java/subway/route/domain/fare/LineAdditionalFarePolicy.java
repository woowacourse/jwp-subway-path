package subway.route.domain.fare;

import subway.route.domain.RouteSegment;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class LineAdditionalFarePolicy implements FarePolicy {

    private static final String ROUTE_FACTOR_KEY = "route";

    @Override
    public void buildFareFactors(FareFactors fareFactors, List<RouteSegment> route, int distance, int age) {
        fareFactors.setFactor(ROUTE_FACTOR_KEY, route);
    }

    @Override
    public int calculate(FareFactors fareFactors, int fare) {
        final List<RouteSegment> route = (List<RouteSegment>) fareFactors.getFactor(ROUTE_FACTOR_KEY);
        validateRoute(route);

        return fare + route.stream()
                           .map(RouteSegment::getLineAdditionalFare)
                           .max(Comparator.naturalOrder())
                           .orElseThrow(() -> new IllegalArgumentException("디버깅: 경로가 비어있습니다. route: " + route));
    }

    private void validateRoute(List<RouteSegment> route) {
        if (Objects.isNull(route)) {
            throw new IllegalArgumentException("buildFareFactors를 먼저 호출해야 합니다.");
        }
    }
}
