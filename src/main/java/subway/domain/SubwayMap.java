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
            subwayGraph.addEdge(section.getUpper(), section.getLower(), SectionEdge.from(section));
            subwayGraph.setEdgeWeight(SectionEdge.from(section), section.getDistance().getValue());
        }
    }

    public Sections getShortestPath(final Station from, final Station to) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(subwayGraph);
        GraphPath<Station, SectionEdge> shortestPath = dijkstraShortestPath.getPath(from, to);
        List<Station> vertexes = shortestPath.getVertexList();
        List<SectionEdge> edges = shortestPath.getEdgeList();
        Map<Station, Section> pathMap = new HashMap<>();

        for (int i = 0; i < edges.size(); i++) {
            SectionEdge section = edges.get(i);
            if (section.getUpper().equals(vertexes.get(i))) {
                pathMap.put(vertexes.get(i), new Section(section.getLindId(), section.getUpper(), section.getLower(), section.getDistance()));
                continue;
            }
            pathMap.put(vertexes.get(i), new Section(section.getLindId(), section.getLower(), section.getUpper(), section.getDistance()));
        }

        return new Sections(pathMap);
    }
}
