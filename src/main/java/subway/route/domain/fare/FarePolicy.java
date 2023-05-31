package subway.route.domain.fare;

import subway.route.domain.RouteSegment;

import java.util.List;

public interface FarePolicy {

    void buildFareFactors(FareFactors fareFactors, List<RouteSegment> route, int distance, int age);

    int calculate(FareFactors fareFactors, int fare);
}
