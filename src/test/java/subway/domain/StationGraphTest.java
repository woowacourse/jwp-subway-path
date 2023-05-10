package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static subway.fixture.LineFixture.LINE_999;
import static subway.fixture.StationFixture.JAMSIL_SAENAE_STATION;
import static subway.fixture.StationFixture.JAMSIL_STATION;

class StationGraphTest {

    @Test
    @DisplayName("최초의 역 2개를 생성할 수 있다.")
    void createNewLineTest() {
        StationGraph stationGraph = createNewLineWithTwoStations();

        Assertions.assertThat(stationGraph.findAllStations()).contains(JAMSIL_STATION, JAMSIL_SAENAE_STATION);
    }

//    @Test
//    @DisplayName("기존 역에 다른 역을 추가할 수 있다.")
//    void createNewLine2() {
//        StationGraph stationGraph = addInitialStations();
//        stationGraph.addStation(JAMSIL_SAENAE_STATION, SPORTS_COMPLEX_STATION, 2);
//
//        Assertions.assertThat(stationGraph.findAllStations()).contains(
//                JAMSIL_STATION, JAMSIL_SAENAE_STATION, SPORTS_COMPLEX_STATION);
//    }

    @Test
    @DisplayName("역 두개의 거리를 확인 할 수 있다.")
    void getDistance() {
        StationGraph stationGraph = createNewLineWithTwoStations();

        Assertions.assertThat(stationGraph.findDistance(JAMSIL_STATION, JAMSIL_SAENAE_STATION)).isEqualTo(3.0);
    }

    private static StationGraph createNewLineWithTwoStations() {
        StationGraph stationGraph = new StationGraph(LINE_999);
        stationGraph.createNewLine(JAMSIL_STATION, JAMSIL_SAENAE_STATION, 3);
        return stationGraph;
    }

    @Test
    @DisplayName("역을 삭제한다.")
    void removeStation() {
        StationGraph stationGraph = createNewLineWithTwoStations();
        stationGraph.removeStation(JAMSIL_STATION);

        Assertions.assertThat(stationGraph.findAllStations()).contains(JAMSIL_SAENAE_STATION);
    }

    @Test
    void 그냥테스트() {

    }
}

