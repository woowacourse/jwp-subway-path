package subway.config;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.Station;

@Configuration
public class PathConfig {

    @Bean
    public Graph<Station, DefaultWeightedEdge> weightedMultigraph() {
        return new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    @Bean
    public ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm() {
        return new DijkstraShortestPath<>(weightedMultigraph());
    }
}
