package subway.domain;

import java.util.List;

public interface RouteFinder {

    List<Station> findRoute(final Station from, final Station to);

    int getDistance(final Station from, final Station to);

    int getSurcharge(final Station from, final Station to);
}
