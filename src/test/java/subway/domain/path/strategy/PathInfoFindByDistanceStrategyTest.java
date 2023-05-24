package subway.domain.path.strategy;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.path.strategy.PathFindByDistanceStrategy;
import subway.domain.subway.Section;

class PathInfoFindByDistanceStrategyTest {
    private final List<Section> sections = List.of(
        new Section(1L, 1L, 2L, 1L, 5),
        new Section(1L, 2L, 3L, 1L, 5),
        new Section(1L, 3L, 4L, 1L, 5),
        new Section(1L, 1L, 3L, 2L, 2),
        new Section(1L, 4L, 5L, 1L, 5),
        new Section(1L, 5L, 6L, 1L, 5),
        new Section(1L, 6L, 7L, 1L, 50)
    );

    @Test
    @DisplayName("최단 경로의 정보를 조회한다.")
    public void getDijkstraShortestPath() {
        PathFindByDistanceStrategy pathFindByDistanceStrategy = new PathFindByDistanceStrategy();

        assertAll(
            () -> assertThat(
                pathFindByDistanceStrategy.findPathInfo(1L, 4L, sections)
                    .getPath()).asList().containsExactly(1L, 3L, 4L),
            () -> assertThat(
                pathFindByDistanceStrategy.findPathInfo(1L, 4L, sections)
                    .getDistance()).isEqualTo(7),
            () -> assertThat(
                pathFindByDistanceStrategy.findPathInfo(1L, 7L, sections)
                    .getPath()).asList().containsExactly(1L, 3L, 4L, 5L, 6L, 7L),
            () -> assertThat(
                pathFindByDistanceStrategy.findPathInfo(1L, 7L, sections)
                    .getDistance()).isEqualTo(67)
        );
    }
}
