package subway.route.domain;

import subway.line.domain.Line;

import java.util.List;

public interface RouteFinderBuilder<T> {

    RouteFinder<T> buildRouteFinder(List<Line> lines);
}
