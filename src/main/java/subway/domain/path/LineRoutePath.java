package subway.domain.path;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

public class LineRoutePath {
    private final List<Station> stations;

    public LineRoutePath(final Sections sections) {
        this.stations = sortSections(sections);
    }

    public LineRoutePath(final Line line) {
        this.stations = sortSections(line.getSections());
    }

    private List<Station> sortSections(final Sections sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = initGraph(sections.getSections());

        final Station frontStation = sections.getFrontStation();
        final Station endStation = sections.getEndStation();

        final DijkstraShortestPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph);
        return path.getPath(frontStation, endStation).getVertexList();
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> initGraph(final List<Section> sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);

        for (final Section section : sections) {
            final Station firstStation = section.getFirstStation();
            final Station secondStation = section.getSecondStation();
            graph.addVertex(firstStation);
            graph.addVertex(secondStation);
            graph.setEdgeWeight(graph.addEdge(firstStation, secondStation), section.getDistance().getDistance());
        }
        return graph;
    }

    public List<Station> getStations() {
        return stations;
    }
}
