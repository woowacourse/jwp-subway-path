package subway.domain;

import java.util.List;

public interface Navigation {

    List<Station> getShortestPath(final Station source, final Station target);

    int getDistance(final Station source, final Station target);
}
