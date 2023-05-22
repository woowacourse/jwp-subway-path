package subway.domain.graph;

import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

public interface Graph {

    void addVertex(Station vertex);
    void addEdge(Station source, Station target, int edge);
    void makeGraph(List<Section> sections);
    List<Station> findShortestPath(Station source, Station target);
    int findShortestPathWeight(Station source, Station target);
}
