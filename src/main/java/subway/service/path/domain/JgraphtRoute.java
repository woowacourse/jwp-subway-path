package subway.service.path.domain;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.service.path.dto.ShortestPath;
import subway.service.section.domain.Section;
import subway.service.station.domain.Station;

import java.util.List;

@Component
public class JgraphtRoute implements PathRouter {

    @Override
    public ShortestPath findShortestPath(List<Section> sections, Station source, Station target) {
        Graph<Station, SectionEdge> weightedGraph = new WeightedMultigraph<>(SectionEdge.class);
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            weightedGraph.addVertex(downStation);
            weightedGraph.addVertex(upStation);
            weightedGraph.addEdge(downStation, upStation, new SectionEdge(section.getDistance()));
        }

        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(weightedGraph);
        GraphPath<Station, SectionEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        return new ShortestPath(shortestPath.getVertexList(), shortestPath.getEdgeList());
    }
}
