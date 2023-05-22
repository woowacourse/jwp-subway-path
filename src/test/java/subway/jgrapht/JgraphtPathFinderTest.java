package subway.jgrapht;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.Fixture;
import subway.domain.Path;
import subway.domain.PathFinder;
import subway.domain.Section;

class JgraphtPathFinderTest {

	@Test
	@DisplayName("출발역과 도착역을 알려주면 최단 거리를 반환한다.")
	void findShortestPathTest() {
		//given
		final PathFinder pathFinder = new JgraphtPathFinder(
			new WeightedMultigraph<>(DefaultWeightedEdge.class));

		List<Section> allSections = new ArrayList<>();
		allSections.addAll(Fixture.LINE_NUMBER_2);
		allSections.addAll(Fixture.LINE_NUMBER_9);

		System.out.println("allSections = " + allSections);


		pathFinder.initialize(allSections);

		//when
		final Path shortestPath = pathFinder.findShortestPath(Fixture.잠실역, Fixture.석촌역);

		//then
		assertThat(shortestPath.getPath()).contains(Fixture.잠실역, Fixture.잠실새내역, Fixture.종합운동장역, Fixture.석촌역);
		assertThat(shortestPath.getDistance().getValue()).isEqualTo(24);
	}
}
