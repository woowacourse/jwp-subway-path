package subway.common.config;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.line.domain.navigation.infrastructure.JgraphtGraph;
import subway.line.domain.navigation.SubwayGraph;

@Configuration
public class NavigationConfig {
    @Bean
    public SubwayGraph subwayGraph() {
        return new JgraphtGraph(new WeightedMultigraph<>(DefaultWeightedEdge.class));
    }
}
