package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static subway.fixture.LineFixture.LINE_2;
import static subway.fixture.StationFixture.*;

class StationGraphTest {

    @Test
    @DisplayName("최초의 역 2개를 생성할 수 있다.")
    void addStationTest() {
        StationGraph stationGraph = addInitialStations();

        Assertions.assertThat(stationGraph.findAllStations()).contains(JAMSIL_STATION, JAMSIL_SAENAE_STATION);
    }

    @Test
    @DisplayName("기존 역에 다른 역을 추가할 수 있다.")
    void addStationTest2() {
        StationGraph stationGraph = addInitialStations();
        stationGraph.addStation(JAMSIL_SAENAE_STATION, SPORTS_COMPLEX_STATION, 2.5);

        Assertions.assertThat(stationGraph.findAllStations()).contains(
                JAMSIL_STATION, JAMSIL_SAENAE_STATION, SPORTS_COMPLEX_STATION);
    }

    @Test
    @DisplayName("역 두개의 거리를 확인 할 수 있다.")
    void getDistance() {
        StationGraph stationGraph = addInitialStations();

        Assertions.assertThat(stationGraph.findDistance(JAMSIL_STATION, JAMSIL_SAENAE_STATION)).isEqualTo(3.0);
    }

    private static StationGraph addInitialStations() {
        StationGraph stationGraph = new StationGraph(LINE_2);
        stationGraph.addStation(JAMSIL_STATION, JAMSIL_SAENAE_STATION, 3.0);
        return stationGraph;
    }
}
