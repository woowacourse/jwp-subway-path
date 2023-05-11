package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.dto.LineDto;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.LineFixture.LINE_999;
import static subway.fixture.StationFixture.*;

class SubwayGraphsTest {

    @Test
    @DisplayName("새로운 노선을 추가할 수 있다.")
    void createLineTest() {
        SubwayGraphs subwayGraphs = new SubwayGraphs();
        LineDto lineDto = subwayGraphs.createLine(LINE_999, EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);

        assertThat(lineDto.getLine()).isEqualTo(LINE_999);
        assertThat(lineDto.getAllStationsInOrder()).containsExactly(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION);
    }

    @Test
    @DisplayName("기존 노선에 새로운 역을 추가할 수 있다.")
    void addStationTest() {
        final SubwayGraphs subwayGraphs = new SubwayGraphs();
        subwayGraphs.createLine(LINE_999, EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);

        final LineDto lineDto = subwayGraphs.addStation(LINE_999, EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 2);

        assertThat(lineDto.getLine()).isEqualTo(LINE_999);
        assertThat(lineDto.getAllStationsInOrder()).containsExactly(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, SAPYEONG_STATION);
    }
}
