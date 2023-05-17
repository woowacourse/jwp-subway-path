package subway.domain;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.exception.NotFoundPathException;

@Component
public class JgraphtPathFinder implements PathFinder {

    @Override
    public Path findShortestPath(final Station startStation,
                                 final Station endStation,
                                 final List<Line> lines) {
        DijkstraShortestPath pathMap = getPathMap(lines);
        GraphPath<Station, SectionProxy> shortestGraph = findShortestGraph(startStation, endStation, pathMap);
        Sections sections = shortestGraph.getEdgeList().stream()
                .map(SectionProxy::toSection)
                .collect(collectingAndThen(toList(), Sections::new));
        return new Path(sections, (int) shortestGraph.getWeight());
    }

    private DijkstraShortestPath getPathMap(List<Line> lines) {
        WeightedMultigraph<Station, SectionProxy> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Line line : lines) {
            for (Section section : line.getSections()) {
                SectionProxy sectionProxy = SectionProxy.from(section);
                graph.addVertex(sectionProxy.getSource());
                graph.addVertex(sectionProxy.getTarget());
                graph.addEdge(sectionProxy.getSource(), sectionProxy.getTarget(), sectionProxy);
                graph.setEdgeWeight(sectionProxy, sectionProxy.getWeight());
            }
        }

        return new DijkstraShortestPath(graph);
    }

    private GraphPath findShortestGraph(Station startStation, Station endStation, DijkstraShortestPath path) {
        GraphPath shortestPath = getPath(startStation, endStation, path);

        if (shortestPath == null) {
            throw new NotFoundPathException();
        }
        return shortestPath;
    }

    private GraphPath getPath(Station startStation, Station endStation, DijkstraShortestPath path) {
        try {
            return path.getPath(startStation, endStation);
        } catch (IllegalArgumentException e) {
            throw new NotFoundPathException();
        }
    }
}
