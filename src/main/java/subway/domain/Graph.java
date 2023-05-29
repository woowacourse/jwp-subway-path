package subway.domain;

import java.util.List;

public interface Graph {

    void setGraph(Sections sections);

    Long findShortestDistance(String upStation, String downStation);

    List<String> findShortestPathInfo(String upStation, String downStation);

}
