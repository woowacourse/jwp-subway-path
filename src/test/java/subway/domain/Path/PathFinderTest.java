package subway.domain.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.Path.PathTestFixture.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;

import java.util.List;

@DisplayName("PathFinder 테스트")
class PathFinderTest {

    @Test
    @DisplayName("출발지와 목적지 역이 주어지면 최단 경로를 찾아서 반환한다.")
    void shortestPathFinderTest() {
        // given
        List<Section> 전체_노선_구간들 = List.of(강변_잠실나루, 잠실나루_잠실, 강동구청_몽촌토성, 몽촌토성_잠실, 잠실_석촌);
        PathFinder pathFinder = PathFinder.from(전체_노선_구간들);

        // when
        Path shortestPath = pathFinder.findShortestPath(잠실나루, 강동구청);

        // then
        assertThat(shortestPath.getOrderedStations()).containsExactly(잠실나루, 잠실, 몽촌토성, 강동구청);
    }

    @Test
    @DisplayName("출발지와 목적지 역이 동일한 역으로 주어지면 예외처리한다.")
    void validateSameStationPathFinderTest() {
        // given
        List<Section> 전체_노선_구간들 = List.of(강변_잠실나루, 잠실나루_잠실, 강동구청_몽촌토성, 몽촌토성_잠실, 잠실_석촌);
        PathFinder pathFinder = PathFinder.from(전체_노선_구간들);

        // then
        assertThatThrownBy(() -> pathFinder.findShortestPath(잠실나루, 잠실나루))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 동일한 역 간 경로 조회는 불가능합니다.");
    }
}
