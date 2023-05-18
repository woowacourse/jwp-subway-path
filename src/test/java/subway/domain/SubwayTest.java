package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.TestFixture.LINE_A;
import static subway.TestFixture.LINE_B;
import static subway.TestFixture.LINE_C;
import static subway.TestFixture.LINE_D;
import static subway.TestFixture.SHORTEST_PATH_IN_LINE_A_AND_B_STATION_A_TO_E;
import static subway.TestFixture.SHORTEST_PATH_IN_LINE_C_AND_D_STATION_A_TO_E;
import static subway.TestFixture.STATION_A;
import static subway.TestFixture.STATION_E;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SubwayTest {

    @DisplayName("역부터 역까지 하행 최단 경로를 알아낸다")
    @Test
    void getShortestPath() {
        var subway = new Subway(List.of(LINE_A, LINE_B));

        Path shortestPath = subway.getShortestPath(STATION_A, STATION_E);

        assertThat(shortestPath.getSections()).isEqualTo(SHORTEST_PATH_IN_LINE_A_AND_B_STATION_A_TO_E);
    }

    @DisplayName("역부터 역까지 상행 최단 경로를 알아낸다")
    @Test
    void getShortestPath_goUp() {
        var subway = new Subway(List.of(LINE_A, LINE_B));
        List<Section> expectedSections = new ArrayList<>(SHORTEST_PATH_IN_LINE_A_AND_B_STATION_A_TO_E);
        Collections.reverse(expectedSections);

        Path shortestPath = subway.getShortestPath(STATION_E, STATION_A);

        assertThat(shortestPath.getSections()).isEqualTo(expectedSections);
    }

    @DisplayName("역부터 역까지 하행 최단 경로를 알아낸다 - 겹치면서 거리만 다른 구간이 있는 경우")
    @Test
    void getShortestPath_withSameStationSection() {
        var subway = new Subway(List.of(LINE_C, LINE_D));

        Path shortestPath = subway.getShortestPath(STATION_A, STATION_E);

        assertThat(shortestPath.getSections()).isEqualTo(SHORTEST_PATH_IN_LINE_C_AND_D_STATION_A_TO_E);
    }

    @DisplayName("역부터 역까지 상행 최단 경로를 알아낸다 - 겹치면서 거리만 다른 구간이 있는 경우")
    @Test
    void getShortestPath_withSameStationSection_goUp() {
        var subway = new Subway(List.of(LINE_C, LINE_D));
        List<Section> expectedSections = new ArrayList<>(SHORTEST_PATH_IN_LINE_C_AND_D_STATION_A_TO_E);
        Collections.reverse(expectedSections);

        Path shortestPath = subway.getShortestPath(STATION_E, STATION_A);

        assertThat(shortestPath.getSections()).isEqualTo(expectedSections);
    }
}
