package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationConnectionsTest {

    @DisplayName("순서가 다른 Section 리스트를 전달받아도 동일한 연결 정보를 가진 객체가 생성된다.")
    @Test
    void create() {
        StationConnections stationConnections = StationConnections.from(List.of(
                SectionFixture.SECTION_START,
                SectionFixture.SECTION_MIDDLE_1,
                SectionFixture.SECTION_MIDDLE_2,
                SectionFixture.SECTION_MIDDLE_3,
                SectionFixture.SECTION_END
        ));

        StationConnections expected = StationConnections.from(List.of(
                SectionFixture.SECTION_END,
                SectionFixture.SECTION_MIDDLE_2,
                SectionFixture.SECTION_START,
                SectionFixture.SECTION_MIDDLE_3,
                SectionFixture.SECTION_MIDDLE_1
        ));

        assertThat(stationConnections.getConnectionsByStation()).containsAllEntriesOf(
                expected.getConnectionsByStation());
    }

    @DisplayName("중복된 구간이 있을 때 노선의 연결정보를 생성할 수 없다.")
    @Test
    void createFail_1() {
        assertThatThrownBy(() -> StationConnections.from(List.of(
                SectionFixture.SECTION_START,
                SectionFixture.SECTION_MIDDLE_1,
                SectionFixture.SECTION_MIDDLE_1
        )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("구간을 중복으로 저장할 수 없습니다.");
    }

    @DisplayName("한 역이 3번 이상 연결될 때 노선의 연결정보를 생성할 수 없다.")
    @Test
    void createFail_2() {
        assertThatThrownBy(() -> StationConnections.from(List.of(
                new Section(StationFixture.FIXTURE_STATION_1, StationFixture.FIXTURE_STATION_2, new Distance(10)),
                new Section(StationFixture.FIXTURE_STATION_2, StationFixture.FIXTURE_STATION_4, new Distance(10)),
                new Section(StationFixture.FIXTURE_STATION_2, StationFixture.FIXTURE_STATION_3, new Distance(10))
        )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("구간을 중복으로 저장할 수 없습니다.");
    }

    @DisplayName("분리된 연결이 있을 때 노선의 연결정보를 생성할 수 없다.")
    @Test
    void createFail_3() {
        assertThatThrownBy(() -> StationConnections.from(List.of(
                SectionFixture.SECTION_START,
                SectionFixture.SECTION_MIDDLE_3
        )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("구간은 하나의 연결로 이루어져 있어야 합니다.");
    }

    @DisplayName("시작 역을 찾을 수 있다.")
    @Test
    void findStartStation() {
        StationConnections stationConnections = StationConnections.from(List.of(
                SectionFixture.SECTION_START,
                SectionFixture.SECTION_MIDDLE_1,
                SectionFixture.SECTION_MIDDLE_2,
                SectionFixture.SECTION_MIDDLE_3,
                SectionFixture.SECTION_END
        ));

        assertThat(stationConnections.findStartStation()).isEqualTo(StationFixture.FIXTURE_STATION_1);
    }

    @DisplayName("끝 역을 찾을 수 있다.")
    @Test
    void findEndStation() {
        StationConnections stationConnections = StationConnections.from(List.of(
                SectionFixture.SECTION_START,
                SectionFixture.SECTION_MIDDLE_1,
                SectionFixture.SECTION_MIDDLE_2,
                SectionFixture.SECTION_MIDDLE_3,
                SectionFixture.SECTION_END
        ));

        assertThat(stationConnections.findEndStation()).isEqualTo(StationFixture.FIXTURE_STATION_6);
    }
}
