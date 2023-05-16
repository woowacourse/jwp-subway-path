package subway.route.domain;

import subway.station.domain.Station;

import java.util.List;

public interface RouteFinder<T> {

    List<T> getRoute(Station source, Station destination);

    int getTotalWeight(Station source, Station destination);
}
