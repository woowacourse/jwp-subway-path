package subway.service.section.domain;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.service.station.domain.Station;

import java.util.List;

public class JgraphtRoute {

    private final List<Station> stations;
    private final double distance;

    private JgraphtRoute(List<Station> stations, double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static JgraphtRoute from(List<Section> sections, Station source, Station target) {
        Graph<Station, SectionEdge> weightedGraph = new WeightedMultigraph<>(SectionEdge.class);
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            weightedGraph.addVertex(downStation);
            weightedGraph.addVertex(upStation);
            weightedGraph.addEdge(downStation, upStation, new SectionEdge(section.getDistance()));
        }

        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(weightedGraph);
        GraphPath<Station, SectionEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        return new JgraphtRoute(shortestPath.getVertexList(), shortestPath.getWeight());
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }
}
