package subway.domain;

import java.util.List;

public interface ShortestPathAlgorithmStrategy {

    List<Station> getShortestPath(Sections sections, Station startStation, Station endStation);

    Distance getShortestPathWeight(Sections sections, Station startStation, Station endStation);

}
