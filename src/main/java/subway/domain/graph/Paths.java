package subway.domain.graph;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Paths {
    private final List<Path> paths;

    private Paths(final List<Path> paths) {
        this.paths = new ArrayList<>(paths);
    }

    public static Paths from(final Lines lines) {
        final List<Section> sections = lines.getAllSections();
        final List<Path> paths = sections.stream()
                .map(Path::new)
                .collect(Collectors.toList());
        return new Paths(paths);
    }

    public void addAllToGraph(final WeightedGraph<Station, DefaultWeightedEdge> graph) {
        paths.forEach(path -> graph.addEdge(path.getSource(), path.getTarget(), path));
    }
}
