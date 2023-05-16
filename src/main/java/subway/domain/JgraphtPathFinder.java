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
    public Path find(final Station startStation, final Station endStation, List<Line> lines) {
        DijkstraShortestPath path = getPath(lines);
        GraphPath<Station, SectionProxy> shortestPath = findShortestPath(startStation, endStation, path);
        Sections sections = shortestPath.getEdgeList().stream()
                .map(SectionProxy::toSection)
                .collect(collectingAndThen(toList(), Sections::new));
        return new Path(sections, (int) shortestPath.getWeight());
    }

    private DijkstraShortestPath getPath(List<Line> lines) {
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

    private static GraphPath findShortestPath(Station startStation, Station endStation, DijkstraShortestPath path) {
        GraphPath shortestPath = path.getPath(startStation, endStation);
        if (shortestPath == null) {
            throw new NotFoundPathException();
        }
        return shortestPath;
    }
}
