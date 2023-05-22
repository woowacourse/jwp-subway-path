package subway.domain.graph;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubwayConfig {
    @Bean
    public SubwayGraph subwayGraph() {
        return new SubwayGraph(new WeightedMultigraph<>(DefaultWeightedEdge.class));
    }
}
