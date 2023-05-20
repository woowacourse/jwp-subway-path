package subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Station;

import java.util.Collections;
import java.util.List;

public class JgraphtPath implements Path {

    private final WeightedMultigraph<Station, SectionEdge> path;

    public JgraphtPath() {
        this.path = new WeightedMultigraph<>(SectionEdge.class);
    }

    public void addStation(Station station) {
        path.addVertex(station);
    }

    public void addSectionEdge(Station sourceStation, Station targetStation, SectionEdge sectionEdge) {
        path.addEdge(sourceStation, targetStation, sectionEdge);
    }

    public List<SectionEdge> getShortestSectionPath(Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Station, SectionEdge> shortestPath = new DijkstraShortestPath<>(path);
        return shortestPath.getPath(sourceStation, targetStation).getEdgeList();
    }

    public List<Station> getShortestStationPath(Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Station, SectionEdge> shortestPath = new DijkstraShortestPath<>(path);
        GraphPath<Station, SectionEdge> graphPath = shortestPath.getPath(sourceStation, targetStation);
        if (graphPath == null) {
            return Collections.emptyList();
        }
        return graphPath.getVertexList();
    }
}
