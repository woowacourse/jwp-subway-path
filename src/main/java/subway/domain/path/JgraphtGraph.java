package subway.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

@Component
@Scope("prototype")
public class JgraphtGraph implements Graph {
    WeightedMultigraph<String, DefaultWeightedEdge> graph;

    public JgraphtGraph() {
        this.graph =  new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    @Override
    public Path findPath(List<Station> stations, List<Section> sections, String start, String end){
        set(graph,stations,sections);
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        List<String> stationsName = dijkstraShortestPath.getPath(start, end).getVertexList();
        double weight = dijkstraShortestPath.getPath(start, end).getWeight();

        return new Path(stationsName, (int)weight);
    }

    private void set(WeightedMultigraph<String, DefaultWeightedEdge> graph, List<Station> stations, List<Section> sections) {
        for (Station station: stations) {
            graph.addVertex(station.getName());
        }
        for (Section section: sections) {
            graph.setEdgeWeight(graph.addEdge(section.getStartStation().getName(),
                            section.getEndStation().getName()),
                    section.getDistance().getDistance());
        }
    }
}
