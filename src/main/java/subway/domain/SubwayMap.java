package subway.domain;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubwayMap {

    private final Graph<Station, SectionEdge> subwayGraph = new SimpleWeightedGraph<>(SectionEdge.class);

    public SubwayMap(final List<Station> stations, final List<Section> sections) {

        for (Station station : stations) {
            subwayGraph.addVertex(station);
        }

        for (Section section : sections) {
            SectionEdge edge = new SectionEdge(section);
            subwayGraph.addEdge(section.getUpper(), section.getLower(), edge);
            subwayGraph.setEdgeWeight(edge, section.getDistance().getValue());
        }
    }

    public Sections getShortestPath(final Station from, final Station to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("존재하지 않는 역입니다.");
        }

        DijkstraShortestPath<Station, SectionEdge> shortestPath = new DijkstraShortestPath<>(subwayGraph);
        GraphPath<Station, SectionEdge> shortestGraph = shortestPath.getPath(from, to);
        List<Station> vertexes = shortestGraph.getVertexList();
        List<SectionEdge> edges = shortestGraph.getEdgeList();
        Map<Station, Section> pathMap = new HashMap<>();

        for (int i = 0; i < edges.size(); i++) {
            SectionEdge section = edges.get(i);
            if (section.getSourceVertex().equals(vertexes.get(i))) {
                pathMap.put(vertexes.get(i), new Section(section.getLineId(), section.getUpper(), section.getLower(), section.getDistance()));
                continue;
            }
            pathMap.put(vertexes.get(i), new Section(section.getLineId(), section.getLower(), section.getUpper(), section.getDistance()));
        }

        return new Sections(pathMap);
    }
}
