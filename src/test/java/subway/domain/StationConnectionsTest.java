package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
