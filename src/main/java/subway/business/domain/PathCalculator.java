package subway.business.domain;

import java.util.List;

public interface PathCalculator {
    int getTotalDistance(Station sourceStation, Station destStation);

    List<Station> getShortestPath(Station sourceStation, Station destStation);
}
