package subway.domain.path;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.SectionProxy;
import subway.domain.Sections;
import subway.domain.Station;
import subway.exception.NotFoundPathException;

@Component
public class JgraphtPathFinder implements PathFinder {

    @Override
    public Path findShortestPath(
            final Station startStation,
            final Station endStation,
            final Lines lines
    ) {
        final DijkstraShortestPath pathMap = getPathMap(lines);
        final GraphPath<Station, SectionProxy> shortestGraph = findShortestGraph(startStation, endStation, pathMap);
        final Sections sections = shortestGraph.getEdgeList().stream()
                .map(SectionProxy::toSection)
                .collect(collectingAndThen(toList(), Sections::new));
        return new Path(sections, (int) shortestGraph.getWeight());
    }

    private DijkstraShortestPath getPathMap(final Lines lines) {
        final WeightedMultigraph<Station, SectionProxy> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Line line : lines.getLines()) {
            for (Section section : line.getSections()) {
                final SectionProxy sectionProxy = SectionProxy.from(section);
                graph.addVertex(sectionProxy.getSource());
                graph.addVertex(sectionProxy.getTarget());
                graph.addEdge(sectionProxy.getSource(), sectionProxy.getTarget(), sectionProxy);
                graph.setEdgeWeight(sectionProxy, sectionProxy.getWeight());
            }
        }

        return new DijkstraShortestPath(graph);
    }

    private GraphPath findShortestGraph(
            final Station startStation,
            final Station endStation,
            final DijkstraShortestPath path
    ) {
        final GraphPath shortestPath = getPath(startStation, endStation, path);

        if (shortestPath == null) {
            throw new NotFoundPathException();
        }
        return shortestPath;
    }

    private GraphPath getPath(
            final Station startStation,
            final Station endStation,
            final DijkstraShortestPath path
    ) {
        try {
            return path.getPath(startStation, endStation);
        } catch (IllegalArgumentException e) {
            throw new NotFoundPathException();
        }
    }
}
