package subway.domain.path;

import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

public class DijkstraShortestPathFinder implements ShortestPathFinder {

    @Override
    public Path find(final List<Section> allSections, final Station startStation, final Station endStation) {
        final WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        return null;
    }
}
