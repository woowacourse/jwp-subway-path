package subway.domain.Path;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.Path.PathTestFixture.강동구청;
import static subway.domain.Path.PathTestFixture.강동구청_몽촌토성;
import static subway.domain.Path.PathTestFixture.강변;
import static subway.domain.Path.PathTestFixture.강변_잠실나루;
import static subway.domain.Path.PathTestFixture.몽촌토성;
import static subway.domain.Path.PathTestFixture.몽촌토성_잠실;
import static subway.domain.Path.PathTestFixture.석촌;
import static subway.domain.Path.PathTestFixture.잠실;
import static subway.domain.Path.PathTestFixture.잠실_석촌;
import static subway.domain.Path.PathTestFixture.잠실나루;
import static subway.domain.Path.PathTestFixture.잠실나루_잠실;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.line.Lines;
import subway.domain.section.Section;
import subway.domain.station.Station;

import java.util.List;

@DisplayName("PathFinder 테스트")
class PathFinderTest {

    @Test
    @DisplayName("출발지와 목적지 역이 주어지면 최단 경로를 찾아서 반환한다.")
    void shortestPathFinderTest() {
        // given
        List<Section> _2호선_구간들 = List.of(강변_잠실나루, 잠실나루_잠실);
        PathFinder pathFinder = PathFinder.from(_2호선_구간들, DistanceFarePolicy.getInstance());

        // when
        List<Station> shortestPath = pathFinder.findShortestPath(강변, 잠실);

        // then
        assertThat(shortestPath).containsExactly(강변, 잠실나루, 잠실);
    }

    @Test
    @DisplayName("출발지와 목적지 역이 주어지면 최단 경로 거리를 찾아서 반환한다.")
    void shortestPathDistanceFinderTest() {
        // given
        List<Section> _2호선_구간들 = List.of(강변_잠실나루, 잠실나루_잠실);
        PathFinder pathFinder = PathFinder.from(_2호선_구간들, DistanceFarePolicy.getInstance());

        // when
        int shortestDistance = pathFinder.findShortestDistance(강변, 잠실);

        // then
        assertThat(shortestDistance).isEqualTo(10);
    }

    @Test
    @DisplayName("출발지와 목적지 역이 동일한 역으로 주어지면 예외처리한다.")
    void validateSameStationPathFinderTest() {
        // given
        List<Section> 전체_노선_구간들 = List.of(강변_잠실나루, 잠실나루_잠실, 강동구청_몽촌토성, 몽촌토성_잠실, 잠실_석촌);
        PathFinder pathFinder = PathFinder.from(전체_노선_구간들, DistanceFarePolicy.getInstance());

        // then
        assertThatThrownBy(() -> pathFinder.findShortestPath(잠실나루, 잠실나루))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 동일한 역 간 경로 조회는 불가능합니다.");
    }

    @Test
    @DisplayName("한 노선에서 경로 찾기 뿐만 아니라 여러 노선의 환승도 고려한다.")
    void shortestPathFinderConsideringTransferLineTest() {
        // given
        List<Section> 전체_노선_구간들 = List.of(강변_잠실나루, 잠실나루_잠실, 강동구청_몽촌토성, 몽촌토성_잠실, 잠실_석촌);
        PathFinder pathFinder = PathFinder.from(전체_노선_구간들, DistanceFarePolicy.getInstance());

        // when
        List<Station> shortestPath = pathFinder.findShortestPath(잠실나루, 강동구청);

        // then
        assertThat(shortestPath).containsExactly(잠실나루, 잠실, 몽촌토성, 강동구청);
    }

    @Test
    @DisplayName("한 노선에서 경로 거리 계산 뿐만 아니라 여러 노선의 환승 거리도 계산한다.")
    void shortestPathDistanceConsideringTransferLineTest() {
        // given
        List<Section> 전체_노선_구간들 = List.of(강변_잠실나루, 잠실나루_잠실, 강동구청_몽촌토성, 몽촌토성_잠실, 잠실_석촌);
        PathFinder pathFinder = PathFinder.from(전체_노선_구간들, DistanceFarePolicy.getInstance());

        // when
        int shortestDistance = pathFinder.findShortestDistance(잠실나루, 강동구청);

        // then
        assertThat(shortestDistance).isEqualTo(26);
    }


    @Nested
    class FareTest {
        
        @Test
        @DisplayName("기본 운임 + 노선별 추가 요금 계산 (2호선 환승X)")
        void fareTest1() {
            // given
            List<Section> 전체_노선_구간들 = List.of(강변_잠실나루, 잠실나루_잠실, 강동구청_몽촌토성, 몽촌토성_잠실, 잠실_석촌);
            PathFinder pathFinder = PathFinder.from(전체_노선_구간들, DistanceFarePolicy.getInstance());

            // when
            int fare = pathFinder.calculateFare(강변, 잠실);

            // then
            assertThat(fare).isEqualTo(2250);
        }

        @Test
        @DisplayName("기본 운임 + 노선별 추가 요금 계산 (8호선 환승X)")
        void fareTest2() {
            // given
            List<Section> 전체_노선_구간들 = List.of(강변_잠실나루, 잠실나루_잠실, 강동구청_몽촌토성, 몽촌토성_잠실, 잠실_석촌);
            PathFinder pathFinder = PathFinder.from(전체_노선_구간들, DistanceFarePolicy.getInstance());

            // when
            int fare = pathFinder.calculateFare(석촌, 강동구청);

            // then
            assertThat(fare).isEqualTo(2150);
        }

        @Test
        @DisplayName("기본 운임 + 노선별 추가 요금 계산 (2호선 -> 8호선 환승O)")
        void fareTest3() {
            // given
            List<Section> 전체_노선_구간들 = List.of(강변_잠실나루, 잠실나루_잠실, 강동구청_몽촌토성, 몽촌토성_잠실, 잠실_석촌);
            PathFinder pathFinder = PathFinder.from(전체_노선_구간들, DistanceFarePolicy.getInstance());

            // when
            int fare = pathFinder.calculateFare(강변, 석촌);

            // then
            assertThat(fare).isEqualTo(2450);
        }
    }
}
