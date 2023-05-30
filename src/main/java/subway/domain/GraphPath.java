package subway.domain;

import java.util.List;

public interface GraphPath {

    List<String> getShortestPath(Sections sections, String fromStationName, String toStationName);

    Distance getShortestDistance(Sections sections, String fromStationName, String toStationName);

}
