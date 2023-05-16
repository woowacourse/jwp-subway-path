package subway;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JgraphtConfiguration {

    @Bean
    public WeightedMultigraph<Long, DefaultWeightedEdge> configureJgrapht() {
        return new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }
}
