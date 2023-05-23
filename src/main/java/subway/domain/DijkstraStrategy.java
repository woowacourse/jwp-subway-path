package subway.domain;

import java.util.List;

public interface DijkstraStrategy {

    List<Station> getShortestPath(Sections sections, Station startStation, Station endStation);

    Distance getShortestPathWeight(Sections sections, Station startStation, Station endStation);

}
