package subway.domain.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.Line;
import subway.domain.Station;

public class Route {

    private static final int MIN_ROUTE_SECTION_SIZE = 1;

    private final List<RouteSection> sections;

    public Route(final List<RouteSection> sections) {
        validateRouteSize(sections);
        validateRouteContinuous(sections);
        this.sections = sections;
    }

    private void validateRouteSize(final List<RouteSection> route) {
        if (route == null || route.size() < MIN_ROUTE_SECTION_SIZE) {
            throw new SubwayIllegalArgumentException("경로에는 1개 이상의 구간이 포함되어야합니다.");
        }
    }

    private void validateRouteContinuous(final List<RouteSection> route) {
        for (int i = 1; i < route.size(); i++) {
            RouteSection left = route.get(i - 1);
            RouteSection right = route.get(i);
            if (!left.getDownStation().equals(right.getUpStation())) {
                throw new SubwayIllegalArgumentException("경로의 구간은 연속되어야합니다.");
            }
        }
    }

    public List<Station> findStationRoute() {
        List<Station> stationRoute = new ArrayList<>();
        stationRoute.add(sections.get(0).getUpStation());
        for (final RouteSection section : sections) {
            stationRoute.add(section.getDownStation());
        }
        return stationRoute;
    }

    public int findTotalDistance() {
        return sections.stream()
                .map(RouteSection::getDistance)
                .reduce(0, Integer::sum);
    }

    public Set<Line> findLines() {
        return sections.stream()
                .map(RouteSection::getLine)
                .collect(Collectors.toSet());
    }
}
