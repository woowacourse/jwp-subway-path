package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static subway.fixture.SectionFixture.SECTION_강남_잠실_5;
import static subway.fixture.SectionFixture.SECTION_길동_암사_3;
import static subway.fixture.SectionFixture.SECTION_몽촌토성_길동_2;
import static subway.fixture.SectionFixture.SECTION_잠실_몽촌토성_5;
import static subway.fixture.StationFixture.STATION_강남;
import static subway.fixture.StationFixture.STATION_강동구청;
import static subway.fixture.StationFixture.STATION_길동;
import static subway.fixture.StationFixture.STATION_몽촌토성;
import static subway.fixture.StationFixture.STATION_잠실;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.graph.JgraphtGraph;
import subway.domain.graph.SubwayGraph;
import subway.domain.section.Section;
import subway.domain.station.Station;

class JgraphtGraphTest {

    private final List<Section> sections = List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5, SECTION_몽촌토성_길동_2,
            SECTION_길동_암사_3);
    private final SubwayGraph graph = new JgraphtGraph();

    @Test
    @DisplayName("두 역의 최단 경로를 구한다.")
    void getPath() {
        List<Station> path = graph.getPath(sections, STATION_강남, STATION_몽촌토성);

        assertThat(path)
                .hasSize(3)
                .containsExactly(STATION_강남, STATION_잠실, STATION_몽촌토성);
    }

    @Test
    @DisplayName("존재하지 않는 역간 경로를 구하려고 하면 예외를 발생시킨다.")
    void getPath_fail() {
        assertThatThrownBy(
                () -> graph.getPath(sections, STATION_강남, STATION_강동구청)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그래프에 역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("최단 경로의 거리를 구한다.")
    void getWeight() {
        int actual = graph.getWeight(sections, STATION_강남, STATION_길동);
        int expected = 12;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 역간 최단 경로의 거리를 구하려고 하면 예외를 발생시킨다.")
    void getWeight_fail() {
        assertThatThrownBy(
                () -> graph.getWeight(sections, STATION_강남, STATION_강동구청)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그래프에 역이 존재하지 않습니다.");
    }
}
