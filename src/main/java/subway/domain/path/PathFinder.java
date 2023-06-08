package subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.section.Section;
import subway.domain.section.SectionEdge;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.subway.Subway;

public class PathFinder {

    private final Subway subway;

    public PathFinder(final Subway subway) {
        this.subway = subway;
    }

    public Path findShortestPath(final Station start, final Station end) {
        final GraphPath<Station, SectionEdge> path = initShortestPath().getPath(start, end);
        return new Path(path.getVertexList(), path.getEdgeList());
    }

    private DijkstraShortestPath<Station, SectionEdge> initShortestPath() {
        final WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        addVertices(graph);
        subway.getSubway().forEach(sections -> setEdges(graph, sections));
        return new DijkstraShortestPath<>(graph);
    }

    private void addVertices(final WeightedMultigraph<Station, SectionEdge> graph) {
        subway.getStations().forEach(graph::addVertex);
    }

    private void setEdges(final WeightedMultigraph<Station, SectionEdge> graph, final Sections sections) {
        for (final Section section : sections.getSections()) {
            SectionEdge edge = new SectionEdge(section, sections.getLineId());
            graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }
}
