package subway.domain.route;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.SectionFixture.잠실_신림_구간_정보;
import static subway.fixture.SectionFixture.잠실_신림_환승_구간_정보;
import static subway.fixture.StationFixture.남위례역;
import static subway.fixture.StationFixture.선릉역;
import static subway.fixture.StationFixture.신림역;
import static subway.fixture.StationFixture.잠실역;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.domain.station.Station;

class ShortestRouteTest {

    @Test
    @DisplayName("출발지에서 도착지로 갈 수 있는 최단경로의 거리르 구한다.")
    void getShortestDistance() {
        // given
        final List<Station> 최단_경로_역들 = List.of(잠실역, 선릉역, 남위례역, 신림역);
        final ShortestRoute shortestRoute = new ShortestRoute(최단_경로_역들);
        final List<Section> 전체_구간 = new ArrayList<>(잠실_신림_구간_정보().getSections());
        전체_구간.addAll(잠실_신림_환승_구간_정보().getSections());

        // when
        final Distance shortestDistance = shortestRoute.getShortestDistance(전체_구간);

        // then
        assertThat(shortestDistance)
            .isEqualTo(new Distance(20));
    }
}
