package study;

import java.util.List;
import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.line.domain.Station;

public class RouteGraphAdapter extends WeightedMultigraph<Station, SectionAdapter> {

    private RouteGraphAdapter() {
        super(SectionAdapter.class);
    }

    public static RouteGraphAdapter adapt(final List<Line> lines) {
        final RouteGraphAdapter routeGraphAdapter = new RouteGraphAdapter();
        lines.stream()
                .flatMap(it -> it.sections().stream())
                .forEach(routeGraphAdapter::addSection);
        return routeGraphAdapter;
    }

    public void addSection(final Section section) {
        addVertex(section.up());
        addVertex(section.down());
        final SectionAdapter sectionAdapter1 = addEdge(section.up(), section.down());
        setEdgeWeight(sectionAdapter1, section.distance());
    }
}
