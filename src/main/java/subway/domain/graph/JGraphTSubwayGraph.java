package subway.domain.graph;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

public class JGraphTSubwayGraph extends WeightedMultigraph<Station, Section> implements SubwayGraph {

    public JGraphTSubwayGraph() {
        super(Section.class);
    }

    @Override
    public void addVertexes(final List<Station> stations) {
        for (Station station : stations) {
            addVertex(station);
        }
    }

    @Override
    public void addEdges(final List<Section> sections) {
        for (Section section : sections) {
            addEdge(section.getUpStation(), section.getDownStation(), section);
            setEdgeWeight(section, section.getDistance().getValue());
        }
    }

    @Override
    public List<Section> getDijkstraShortestPath(final Station from, final Station to) {
        final DijkstraShortestPath<Station, Section> dijkstraShortestPath = new DijkstraShortestPath<>(this);
        return dijkstraShortestPath.getPath(from, to).getEdgeList();
    }
}

