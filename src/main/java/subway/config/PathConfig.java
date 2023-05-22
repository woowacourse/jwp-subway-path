package subway.config;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import subway.domain.Station;

@Configuration
public class PathConfig {

	@Bean
	public WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultigraph() {
		return new WeightedMultigraph<>(DefaultWeightedEdge.class);
	}
}
