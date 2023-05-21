package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static subway.fixture.SubwayFixtures.SUBWAY2;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.exception.StationNotFoundException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PathFinderTest {

    @Test
    void 입력받은_출발역과_도착역의_최단경로를_조회한다_상행에서_하행방향의_경우() {
        // given
        final Subway subway = SUBWAY2;
        PathFinder pathFinder = new PathFinder(subway);

        // when
        final Path shortestPath = pathFinder.findShortestPath("A", "D");

        // then
        assertAll(
                () -> assertThat(shortestPath.getPath()).containsExactly(
                        new Station("A"), new Station("B"), new Station("D")),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(new Distance(4))
        );
    }

    @Test
    void 입력받은_출발역과_도착역의_최단경로를_조회한다_하행에서_상행방향의_경우() {
        // given
        final Subway subway = SUBWAY2;
        PathFinder pathFinder = new PathFinder(subway);

        // when
        final Path shortestPath = pathFinder.findShortestPath("D", "A");

        // then
        assertAll(
                () -> assertThat(shortestPath.getPath()).containsExactly(
                        new Station("D"), new Station("B"), new Station("A")),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(new Distance(4))
        );
    }

    @Test
    void 입력받은_출발역이_존재하지_않으면_예외를_던진다() {
        // given
        final Subway subway = SUBWAY2;
        PathFinder pathFinder = new PathFinder(subway);

        // expect
        assertThatThrownBy(() -> pathFinder.findShortestPath("Q", "B"))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("역을 찾을 수 없습니다.");
    }

    @Test
    void 입력받은_도착역이_존재하지_않으면_예외를_던진다() {
        // given
        final Subway subway = SUBWAY2;
        PathFinder pathFinder = new PathFinder(subway);

        // expect
        assertThatThrownBy(() -> pathFinder.findShortestPath("B", "Q"))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("역을 찾을 수 없습니다.");
    }
}
