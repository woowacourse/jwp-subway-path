package subway.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Stations;

import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class ShortestPathFinder {

    public ShortestPath findShortestPath(Sections sections, Stations stations, Station departure, Station destination) {
        WeightedMultigraph<String, DefaultWeightedEdge> subway = new WeightedMultigraph(DefaultWeightedEdge.class);

        stations.getStations()
                .forEach(station -> subway.addVertex(station.getName()));
        sections.getSections()
                .forEach(section -> subway.setEdgeWeight(subway.addEdge(section.getUpstream().getName(), section.getDownstream().getName()), section.getDistance()));

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(subway);
        List<String> shortestPath = dijkstraShortestPath.getPath(departure.getName(), destination.getName()).getVertexList();
        double shortestPathDistance = dijkstraShortestPath.getPathWeight(departure.getName(), destination.getName());

        return shortestPath.stream()
                .map(Station::new)
                .collect(collectingAndThen(toList(), stations1 -> new ShortestPath(stations1, (int) shortestPathDistance)));
    }
}
