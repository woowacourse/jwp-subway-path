package subway.domain.graph;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.fare.FarePolicy;
import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.List;

public class SubwayGraph {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> graph;
    private final FarePolicy farePolicy;

    public SubwayGraph(final DijkstraShortestPath<Station, DefaultWeightedEdge> graph, final FarePolicy farePolicy) {
        this.graph = graph;
        this.farePolicy = farePolicy;
    }


    public static SubwayGraph of(final List<Line> lines, final FarePolicy farePolicy) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (final Line line : lines) {
            line.stations().forEach(station -> {
                if (!graph.containsVertex(station)) {
                    graph.addVertex(station);
                }
            });
            line.sections()
                    .forEach(section -> {
                        final DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
                        graph.setEdgeWeight(edge, section.getDistanceValue());
                    });
        }

        return new SubwayGraph(new DijkstraShortestPath<>(graph), farePolicy);
    }

    public double getPathWeight(final Station fromStation, final Station toStation) {

        return graph.getPathWeight(fromStation, toStation);
    }

    public List<Station> getPath(final Station fromStation, final Station toStation) {

        return graph.getPath(fromStation, toStation).getVertexList();
    }

    public int getPathFare(final Station fromStation, final Station toStation) {
        final double pathDistance = getPathWeight(fromStation, toStation);

        return farePolicy.calculateOverFare(pathDistance);
    }
}
