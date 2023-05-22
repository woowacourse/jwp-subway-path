package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.TestFixture.STATION_A;
import static subway.TestFixture.STATION_B;
import static subway.TestFixture.STATION_C;
import static subway.TestFixture.STATION_D;
import static subway.TestFixture.STATION_E;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SubwayTest {

    public static final Line LINE_A = new Line(4L, "99호선", "gray");
    public static final Line LINE_B = new Line(5L, "100호선", "white");

    // https://github.com/woowacourse/jwp-subway-path/assets/39221443/a2270a47-f63f-4955-bcd4-b85a00ae5999 참조
    static {
        LINE_A.add(new Section(STATION_A, STATION_C, 1));
        LINE_A.add(new Section(STATION_C, STATION_D, 1));
        LINE_A.add(new Section(STATION_D, STATION_E, 6));
        LINE_B.add(new Section(STATION_A, STATION_B, 1));
        LINE_B.add(new Section(STATION_B, STATION_C, 1));
        LINE_B.add(new Section(STATION_C, STATION_E, 6));
    }

    public static final List<Section> SHORTEST_PATH_IN_LINE_A_AND_B_STATION_A_TO_E = List.of(
            new Section(STATION_A, STATION_C, 1),
            new Section(STATION_C, STATION_E, 6)
    );

    public static final Line LINE_C = new Line(1L, "101호선", "blue");
    public static final Line LINE_D = new Line(1L, "102호선", "blue");

    static {
        LINE_C.add(new Section(STATION_A, STATION_C, 1));
        LINE_C.add(new Section(STATION_C, STATION_D, 3));
        LINE_C.add(new Section(STATION_D, STATION_E, 4));
        LINE_D.add(new Section(STATION_B, STATION_C, 1));
        LINE_D.add(new Section(STATION_C, STATION_D, 1));
    }

    public static final List<Section> SHORTEST_PATH_IN_LINE_C_AND_D_STATION_A_TO_E = List.of(
            new Section(STATION_A, STATION_C, 1),
            new Section(STATION_C, STATION_D, 1),
            new Section(STATION_D, STATION_E, 4)
    );

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
