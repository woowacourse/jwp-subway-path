package subway.domain;

import org.jgrapht.GraphPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.application.exception.SubwayServiceException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SubwaysTest {

    @DisplayName("전체 지하철 노선도를 생성할 수 있다.")
    @Test
    void create() {
        assertDoesNotThrow(() -> Subways.from(List.of(
                SectionFixture.SECTION_START, SectionFixture.SECTION_MIDDLE_1,
                SectionFixture.SECTION_MIDDLE_2, SectionFixture.SECTION_MIDDLE_3,
                SectionFixture.SECTION_END)));
    }

    @DisplayName("하나의 호선 내에서 하나의 경로만 있을 때의 최단 경로를 구할 수 있다.")
    @Test
    void getShortestPath_OneLineOnePath() {
        Section start = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_1,
                StationFixture.FIXTURE_STATION_2, new Distance(5));

        Section middle1 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_2,
                StationFixture.FIXTURE_STATION_3, new Distance(7));

        Section middle2 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_3,
                StationFixture.FIXTURE_STATION_4, new Distance(4));

        Section middle3 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_4,
                StationFixture.FIXTURE_STATION_5, new Distance(2));

        Section end = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_5,
                StationFixture.FIXTURE_STATION_6, new Distance(10));

        Subways subways = Subways.from(List.of(start, middle1, middle2, middle3, end));
        GraphPath<Station, SubwayEdge> shortestPaths = subways.getShortestPaths(StationFixture.FIXTURE_STATION_1, StationFixture.FIXTURE_STATION_6);
        assertAll(
                () -> assertThat(shortestPaths.getVertexList())
                        .containsExactly(
                                StationFixture.FIXTURE_STATION_1, StationFixture.FIXTURE_STATION_2, StationFixture.FIXTURE_STATION_3,
                                StationFixture.FIXTURE_STATION_4, StationFixture.FIXTURE_STATION_5, StationFixture.FIXTURE_STATION_6),
                () -> assertThat((int) shortestPaths.getWeight()).isEqualTo(28)
        );
    }


    @DisplayName("하나의 호선 내에서 여러 경로가 있을 때의 최단 경로를 구할 수 있다.")
    @Test
    void getShortestPath_OneLineMultiPath() {
        Section start = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_1,
                StationFixture.FIXTURE_STATION_2, new Distance(5));

        Section middle1 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_2,
                StationFixture.FIXTURE_STATION_3, new Distance(7));

        Section middle2 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_3,
                StationFixture.FIXTURE_STATION_4, new Distance(4));

        Section middle3 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_4,
                StationFixture.FIXTURE_STATION_5, new Distance(2));

        Section end = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_5,
                StationFixture.FIXTURE_STATION_6, new Distance(10));

        Section shortcut = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_1,
                StationFixture.FIXTURE_STATION_6, new Distance(15));

        Subways subways = Subways.from(List.of(start, middle1, middle2, middle3, end, shortcut));
        GraphPath<Station, SubwayEdge> shortestPaths = subways.getShortestPaths(StationFixture.FIXTURE_STATION_1, StationFixture.FIXTURE_STATION_6);
        assertAll(
                () -> assertThat(shortestPaths.getVertexList())
                        .containsExactly(
                                StationFixture.FIXTURE_STATION_1, StationFixture.FIXTURE_STATION_6),
                () -> assertThat((int) shortestPaths.getWeight()).isEqualTo(15)
        );
    }

    @DisplayName("여러 호선에서 하나의 경로가 있을 때의 최단 경로를 구할 수 있다._환승")
    @Test
    void getShortestPath_MultiLineOnePath_transfer() {
        Section start = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_1,
                StationFixture.FIXTURE_STATION_2, new Distance(5));

        Section middle1 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_2,
                StationFixture.FIXTURE_STATION_3, new Distance(7));

        Section middle2 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_3,
                StationFixture.FIXTURE_STATION_4, new Distance(4));

        Section middle3 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_4,
                StationFixture.FIXTURE_STATION_5, new Distance(2));

        Section end = new Section(
                LineFixture.FIXTURE_LINE_2, StationFixture.FIXTURE_STATION_4,
                StationFixture.FIXTURE_STATION_6, new Distance(10));

        Subways subways = Subways.from(List.of(start, middle1, middle2, middle3, end));
        GraphPath<Station, SubwayEdge> shortestPaths = subways.getShortestPaths(StationFixture.FIXTURE_STATION_1, StationFixture.FIXTURE_STATION_6);
        assertAll(
                () -> assertThat(shortestPaths.getVertexList())
                        .containsExactly(
                                StationFixture.FIXTURE_STATION_1, StationFixture.FIXTURE_STATION_2, StationFixture.FIXTURE_STATION_3,
                                StationFixture.FIXTURE_STATION_4, StationFixture.FIXTURE_STATION_6),
                () -> assertThat((int) shortestPaths.getWeight()).isEqualTo(26)
        );
    }

    @DisplayName("여러 호선에서 여러 경로가 있을 때의 최단 경로를 구할 수 있다._환승")
    @Test
    void getShortestPath_MultiLineMultiPath_transfer() {
        Section start = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_1,
                StationFixture.FIXTURE_STATION_2, new Distance(5));

        Section middle1 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_2,
                StationFixture.FIXTURE_STATION_3, new Distance(7));

        Section middle2 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_3,
                StationFixture.FIXTURE_STATION_4, new Distance(4));

        Section middle3 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_4,
                StationFixture.FIXTURE_STATION_5, new Distance(2));

        Section end = new Section(
                LineFixture.FIXTURE_LINE_2, StationFixture.FIXTURE_STATION_4,
                StationFixture.FIXTURE_STATION_6, new Distance(10));

        Section shortcut = new Section(
                LineFixture.FIXTURE_LINE_2, StationFixture.FIXTURE_STATION_2,
                StationFixture.FIXTURE_STATION_6, new Distance(15));

        Subways subways = Subways.from(List.of(start, middle1, middle2, middle3, end, shortcut));
        GraphPath<Station, SubwayEdge> shortestPaths = subways.getShortestPaths(StationFixture.FIXTURE_STATION_1, StationFixture.FIXTURE_STATION_6);
        assertAll(
                () -> assertThat(shortestPaths.getVertexList())
                        .containsExactly(
                                StationFixture.FIXTURE_STATION_1, StationFixture.FIXTURE_STATION_2, StationFixture.FIXTURE_STATION_6),
                () -> assertThat((int) shortestPaths.getWeight()).isEqualTo(20)
        );
    }

    @DisplayName("노선에 존재하지 않는 역을 전달하면 예외가 발생한다.")
    @Test
    void getShortestPath_noStation() {
        Section start = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_1,
                StationFixture.FIXTURE_STATION_2, new Distance(5));

        Section middle1 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_2,
                StationFixture.FIXTURE_STATION_3, new Distance(7));

        Section middle2 = new Section(
                LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_3,
                StationFixture.FIXTURE_STATION_4, new Distance(4));


        Subways subways = Subways.from(List.of(start, middle1, middle2));

        assertThatThrownBy(() -> subways.getShortestPaths(StationFixture.FIXTURE_STATION_1, StationFixture.FIXTURE_STATION_6))
                .isInstanceOf(SubwayServiceException.class)
                .hasMessageContaining("노선에 존재하지 않는 역을 입력했습니다.");
    }
}
