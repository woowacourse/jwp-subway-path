package subway.domain;

import java.util.List;

public interface Path {
    List<Station> findPath(Station sourceStation, Station targetStation);

    int getDistance(Station sourceStation, Station targetStation);
}
