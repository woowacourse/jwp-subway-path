package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.TestFixture.LINE_A;
import static subway.TestFixture.LINE_B;
import static subway.TestFixture.LINE_C;
import static subway.TestFixture.LINE_D;
import static subway.TestFixture.LINE_E;
import static subway.TestFixture.SHORTEST_PATH_IN_LINE_A_AND_B_STATION_A_TO_E;
import static subway.TestFixture.SHORTEST_PATH_IN_LINE_C_AND_D_STATION_A_TO_E;
import static subway.TestFixture.STATION_A;
import static subway.TestFixture.STATION_D;
import static subway.TestFixture.STATION_E;
import static subway.TestFixture.STATION_F;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.exception.ShortestPathSearchFailedException;

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

    @DisplayName("지하철에 포함되지 않은 역을 넣으면 예외를 던진다")
    @Test
    void notContainedStation_throws() {
        var subway = new Subway(List.of(LINE_D, LINE_E));

        assertThatThrownBy(() -> subway.getShortestPath(STATION_A, STATION_F))
                .isInstanceOf(ShortestPathSearchFailedException.class)
                .hasMessageStartingWith("지하철에 포함되지 않은 역(")
                .hasMessageEndingWith(")이 있습니다");
    }

    @DisplayName("두 역 간의 경로가 전무하면 예외를 던진다")
    @Test
    void noPathBetweenTwoStation_throws() {
        var subway = new Subway(List.of(LINE_D, LINE_E));

        assertThatThrownBy(() -> subway.getShortestPath(STATION_A, STATION_D))
                .isInstanceOf(ShortestPathSearchFailedException.class)
                .hasMessage("두 역간의 경로가 없습니다");
    }
}
