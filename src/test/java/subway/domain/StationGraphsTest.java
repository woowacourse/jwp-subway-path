package subway.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.fixture.LineFixture.LINE_999;
import static subway.fixture.StationFixture.JAMSIL_SAENAE_STATION;
import static subway.fixture.StationFixture.JAMSIL_STATION;

class StationGraphsTest {

    @Test
    @DisplayName("초기 역 2개로 노선 그래프를 생성할 수 있다.")
    void createLineTest() {
        final StationGraphs stationGraphs = StationGraphs.getInstance();

        stationGraphs.createLine(LINE_999, JAMSIL_STATION, JAMSIL_SAENAE_STATION, 3.0);

        final StationGraph stationGraph = stationGraphs.findStationGraphOf(LINE_999);
        assertThat(stationGraph.findAllStations().size()).isEqualTo(2);
    }

    @AfterEach
    void tearDown() {
        final StationGraphs stationGraphs = StationGraphs.getInstance();
        stationGraphs.deleteStationGraphOf(LINE_999);
    }
}
