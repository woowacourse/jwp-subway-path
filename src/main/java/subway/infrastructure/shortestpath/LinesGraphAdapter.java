package subway.infrastructure.shortestpath;

import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Path;
import subway.domain.Section;
import subway.domain.Station;

public class LinesGraphAdapter extends WeightedMultigraph<Station, SectionAdapter> {

    private LinesGraphAdapter() {
        super(SectionAdapter.class);
    }

    public static LinesGraphAdapter adapt(final Path path) {
        final LinesGraphAdapter routeGraphAdapter = new LinesGraphAdapter();
        path.lines().stream()
                .flatMap(it -> it.sections().stream())
                .forEach(routeGraphAdapter::addSection);
        return routeGraphAdapter;
    }

    public void addSection(final Section section) {
        addVertex(section.up());
        addVertex(section.down());
        final SectionAdapter sectionAdapter = addEdge(section.up(), section.down());
        setEdgeWeight(sectionAdapter, section.distance());
    }
}
