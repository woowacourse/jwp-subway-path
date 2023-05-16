package subway.domain.subway;

import java.util.List;
import subway.domain.station.Station;

public interface SubwayGraph {

    List<Station> findShortestPath(final Station start, final Station end);

    long calculateShortestDistance(final Station start, final Station end);
}
