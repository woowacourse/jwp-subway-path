package subway.domain.path;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathFinder {

    public Path findShortestPath(final Sections sections, final Station departureStation, final Station arrivalStation) {
        Graph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        setGraph(graph, sections);
        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        List<Station> path = shortestPath.getPath(departureStation, arrivalStation).getVertexList();
        double distance = shortestPath.getPathWeight(departureStation, arrivalStation);
        return new Path(path, new Distance((int) distance));
    }

    private void setGraph(final Graph<Station, DefaultWeightedEdge> graph, final Sections sections) {
        Set<Station> stations = new HashSet<>(sections.findAllStation());
        for (Station station : stations) {
            graph.addVertex(station);
        }

        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getLeftStation(), section.getRightStation()),
                    section.getDistance());
        }
    }
}
