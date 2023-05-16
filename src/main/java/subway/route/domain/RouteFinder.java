package subway.route.domain;

import java.util.List;

public interface RouteFinder <T> {

    List<T> getRoute(long source, long destination);

    int getTotalWeight();
}
