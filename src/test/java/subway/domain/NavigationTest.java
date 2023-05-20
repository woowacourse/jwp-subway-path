package subway.domain;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NavigationTest {

    @Test
    void 최단_경로를_조회한다() {
        // given
        final Section firstSection = new Section("잠실역", "석촌역", 2);
        final Section secondSection = new Section("석촌역", "송파역", 2);
        final Section thirdSection = new Section("잠실역", "송파역", 10);
        final Navigation navigation = Navigation.from(List.of(
                new Sections(List.of(firstSection, secondSection, thirdSection)))
        );

        // when
        final List<Station> shortestPath = navigation.getShortestPath(new Station("잠실역"), new Station("석촌역"));

        // then
        assertThat(shortestPath).isEqualTo(List.of(new Station("잠실역"), new Station("석촌역")));
    }

}
