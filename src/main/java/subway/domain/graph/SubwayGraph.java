package subway.domain.graph;

import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

public interface SubwayGraph {

    void addVertexes(final List<Station> stations);

    void addEdges(final List<Section> sections);

    List<Section> getDijkstraShortestPath(final Station from, final Station to);
}
