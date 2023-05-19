package subway;

import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import subway.domain.pathfinder.LineWeightedEdge;

@Configuration
public class JgraphtConfiguration {

    @Bean
    public WeightedMultigraph<Long, LineWeightedEdge> configureJgrapht() {
        return new WeightedMultigraph<>(LineWeightedEdge.class);
    }
}
