package subway.domain;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final List<Station> pathVerticies;
    private final Sections pathEdges;

    public PathFinder(Sections sections, Station source, Station target) {
        GraphPath<Station, SectionWeightedEdge> shortestPath = getPath(sections, source, target);
        try {
            pathVerticies = shortestPath.getVertexList();
        } catch (NullPointerException exception) {
            throw new PathException("두 역이 연결되지 않았습니다.");
        }
        pathEdges = new Sections(shortestPath.getEdgeList().stream()
            .map(SectionWeightedEdge::toSection)
            .collect(toList()));
    }

    private GraphPath<Station, SectionWeightedEdge> getPath(Sections sections, Station source, Station target) {
        WeightedMultigraph<Station, SectionWeightedEdge> graph = makeGraph(sections);
        DijkstraShortestPath<Station, SectionWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return shortestPath.getPath(source, target);
    }

    public WeightedMultigraph<Station, SectionWeightedEdge> makeGraph(Sections sections) {
        WeightedMultigraph<Station, SectionWeightedEdge> graph = new WeightedMultigraph<>(SectionWeightedEdge.class);

        List<Station> stations = sections.getDistinctStations();
        stations.forEach(graph::addVertex);

        List<SectionWeightedEdge> sectionWeightedEdges = SectionWeightedEdge.toSectionWeightedEdges(sections);
        for (SectionWeightedEdge sectionWeightedEdge : sectionWeightedEdges) {
            graph.addEdge(sectionWeightedEdge.getSource(), sectionWeightedEdge.getTarget(), sectionWeightedEdge);
        }
        return graph;
    }

    public List<Station> getPathVerticies() {
        return pathVerticies;
    }

    public Sections getPathEdges() {
        return pathEdges;
    }
}
