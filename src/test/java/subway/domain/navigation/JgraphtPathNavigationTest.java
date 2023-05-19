package subway.domain.navigation;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.edge.StationEdge;
import subway.domain.line.edge.StationEdges;

import java.util.Set;

class JgraphtPathNavigationTest {

    private JgraphtPathNavigation jgraphtPathNavigation;

    @BeforeEach
    void setUp() {
        jgraphtPathNavigation = new JgraphtPathNavigation();
    }

    @Test
    @DisplayName("그래프에서 경로를 구한다.")
    void find_path_test() {
        // given
        final Set<StationEdge> subwayGraph = Set.of(
                new StationEdge(1L, 2L, 2),
                new StationEdge(2L, 3L, 1),
                new StationEdge(3L, 4L, 3),
                new StationEdge(4L, 5L, 2),
                new StationEdge(6L, 4L, 4),
                new StationEdge(4L, 7L, 5),
                new StationEdge(7L, 8L, 3),
                new StationEdge(3L, 7L, 6)
        );

        // when
        final StationEdges path = jgraphtPathNavigation.findPath(2L, 8L, subwayGraph);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(path.getStationIdsInOrder()).containsExactly(2L, 3L, 7L, 8L);
            softly.assertThat(path.getTotalDistance()).isEqualTo(10);
        });
    }

}
