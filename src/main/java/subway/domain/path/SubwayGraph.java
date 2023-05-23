package subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;

public class SubwayGraph extends WeightedMultigraph<Station, SubwayEdge> {

    private SubwayGraph() {
        super(SubwayEdge.class);
    }

    public static SubwayGraph from(Subway subway) {
        SubwayGraph subwayGraph = new SubwayGraph();
        subway.getLines().stream()
                .flatMap(line -> line.getSections().stream())
                .forEach(subwayGraph::add);
        return subwayGraph;
    }

    private void add(Section section) {
        addVertex(section.getTarget());
        addVertex(section.getSource());
        SubwayEdge subwayEdge = addEdge(section.getSource(), section.getTarget());
        setEdgeWeight(subwayEdge, section.getDistance());
    }

    public ShortestPath findPath(Station source, Station target) {
        DijkstraShortestPath<Station, SubwayEdge> dijkstra = new DijkstraShortestPath<>(this);
        GraphPath<Station, SubwayEdge> path = dijkstra.getPath(source, target);

        int sum = path.getEdgeList().stream()
                .mapToInt(SubwayEdge::getDistance)
                .sum();
        return new ShortestPath(path.getVertexList(), sum);
    }
}
