package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.IllegalStationException;
import subway.exception.StationNotFoundException;

class PathFinderTest {

    private static final Sections sections = new Sections(List.of(
            new Section(1L, new Station("미금역"), new Station("정자역"), Distance.from(3)),
            new Section(2L, new Station("정자역"), new Station("수내역"), Distance.from(4)),
            new Section(3L, new Station("수내역"), new Station("서현역"), Distance.from(5)),
            new Section(4L, new Station("서현역"), new Station("판교역"), Distance.from(4)),

            new Section(6L, new Station("정자역"), new Station("서현역"), Distance.from(6)),
            new Section(7L, new Station("서현역"), new Station("야탑역"), Distance.from(6)),
            new Section(5L, new Station("야탑역"), new Station("판교역"), Distance.from(2)),

            new Section(8L, new Station("복정역"), new Station("잠실역"), Distance.from(10))
    ));

    @Test
    @DisplayName("모든 구간 정보를 가지고 있는 경로 조회 객체를 생성한다.")
    void createPathFinder() {
        // when, then
        assertDoesNotThrow(() -> PathFinder.from(sections));
    }

    @Test
    @DisplayName("출발역과 도착역을 기반으로 하행 방향으로 노선에 상관없이 최단 경로, 거리를 계산한 Path를 반환한다.")
    void calculateShortestPath_upperWay_success() {
        // given
        PathFinder pathFinder = PathFinder.from(sections);

        // when
        Path path = pathFinder.findShortestPath(new Station("판교역"), new Station("정자역"));

        // then
        assertThat(path).usingRecursiveComparison()
                .isEqualTo(Path.of(
                        List.of(new Station("판교역"), new Station("서현역"), new Station("정자역")),
                        Distance.from(10)));
    }

    @Test
    @DisplayName("출발역과 도착역을 기반으로 상행 방향으로 노선에 상관없이 최단 경로, 거리를 계산한 Path를 반환한다.")
    void calculateShortestPath_lowerWay_success() {
        // given
        PathFinder pathFinder = PathFinder.from(sections);

        // when
        Path path = pathFinder.findShortestPath(new Station("미금역"), new Station("수내역"));

        // then
        assertThat(path).usingRecursiveComparison()
                .isEqualTo(Path.of(
                        List.of(new Station("미금역"), new Station("정자역"), new Station("수내역")),
                        Distance.from(7)));
    }

    @Test
    @DisplayName("출발역에서 도착역으로 이동할 수 없다면 예외가 발생한다.")
    void calculateShortestPath_noPath_fail() {
        // given
        PathFinder pathFinder = PathFinder.from(sections);

        // when, then
        assertThatThrownBy(() -> pathFinder.findShortestPath(new Station("서현역"), new Station("복정역")))
                .isInstanceOf(IllegalStationException.class)
                .hasMessageContaining("요청한 출발역과 도착역 사이의 경로가 없습니다.");
    }

    @Test
    @DisplayName("출발역 또는 도착역이 노선에 존재하지 않는다면 예외가 발생한다.")
    void calculateShortestPath_noStationInLine_fail() {
        // given
        PathFinder pathFinder = PathFinder.from(sections);

        // when, then
        assertThatThrownBy(() -> pathFinder.findShortestPath(new Station("장지역"), new Station("정자역")))
                .isInstanceOf(StationNotFoundException.class);
    }
}