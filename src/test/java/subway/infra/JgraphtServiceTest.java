package subway.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.exception.ErrorCode.ROUTE_NOT_EXISTS;
import static subway.fixture.SectionFixture.잠실_신림_구간_정보;
import static subway.fixture.SectionFixture.잠실_신림_환승_구간_정보;
import static subway.fixture.StationFixture.신림역;
import static subway.fixture.StationFixture.잠실역;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.section.SubwayLine;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.exception.BadRequestException;

class JgraphtServiceTest {

    private JgraphtService jgraphtService;

    @BeforeEach
    void setUp() {
        jgraphtService = new JgraphtService();
    }

    @Test
    @DisplayName("모든 구간 정보 중에서 도착지로 갈 수 있는 경로 중 가장 짧은 경로를 구한다.")
    void getShortestPath() {
        // when
        final List<SubwayLine> 지하철_구간_정보들 = List.of(잠실_신림_구간_정보(), 잠실_신림_환승_구간_정보());
        final List<Station> shortestPath = jgraphtService.getShortestPath(지하철_구간_정보들, 잠실역, 신림역);

        // then
        assertThat(shortestPath)
            .extracting(Station::name)
            .extracting(StationName::name)
            .containsExactly("잠실역", "선릉역", "남위례역", "신림역");
    }

    @ParameterizedTest(name = "출발역에서 도착역으로 갈 수 있는 경로가 존재하지 않으면 예외가 발생한다.")
    @CsvSource(value = {"잠실역:바론역", "바론역:신림역"}, delimiter = ':')
    void getShortestPath_exception(final String sourceStationName, final String targetStationName) {
        // given
        final List<SubwayLine> 지하철_구간_정보들 = List.of(잠실_신림_구간_정보(), 잠실_신림_환승_구간_정보());

        // expected
        final Station sourceStation = Station.create(sourceStationName);
        final Station targetStation = Station.create(targetStationName);
        assertThatThrownBy(() -> jgraphtService.getShortestPath(지하철_구간_정보들, sourceStation, targetStation))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ROUTE_NOT_EXISTS);
    }
}
