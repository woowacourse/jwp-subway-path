package subway.domain.route;

import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.Station;

public class DijkstraRouteMap implements RouteMap {
    private final Graph<Station, SubwayRouteEdge> graph = new WeightedMultigraph<>(SubwayRouteEdge.class);

    public DijkstraRouteMap(final Lines lines) {
        makeGraph(lines);
    }

    public DijkstraRouteMap(final Line... lines) {
        this(new Lines(lines));
    }

    private void makeGraph(final Lines lines) {
        for (Line line : lines.getLines()) {
            addLineIntoGraph(line);
        }
    }

    private void addLineIntoGraph(final Line line) {
        final List<Section> sections = line.getSectionsByList();
        for (Section section : sections) {
            final SubwayRouteEdge routeEdge = new SubwayRouteEdge(line);
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.addEdge(upStation, downStation, routeEdge);
            graph.setEdgeWeight(routeEdge, section.getDistance());
        }
    }

    @Override
    public Path findShortestPath(final Station startStation, final Station endStation) {
        DijkstraShortestPath<Station, SubwayRouteEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SubwayRouteEdge> path = dijkstraShortestPath.getPath(startStation, endStation);
        return new Path(path.getVertexList(), (int) path.getWeight());
    }
}
