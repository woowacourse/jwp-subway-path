package subway.section.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.station.domain.Station;

class SectionsTest {

    private void registerStations(final String... names) {
        Station.clear();
        Arrays.stream(names).forEach(Station::register);
    }

    @DisplayName("두개의 역을 이용하여 노선을 초기화 한다.")
    @Test
    void initializeLine() {
        //given
        registerStations("잠실역", "잠실새내역");
        final Sections sections = Sections.empty();

        //when
        sections.initializeSections("잠실새내역", "잠실역", 3);

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(1),
            () -> assertThat(sections.getSections().get(0).getUp()).isEqualTo(Station.get("잠실새내역")),
            () -> assertThat(sections.getSections().get(0).getDown()).isEqualTo(Station.get("잠실역")),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(3)
        );
    }

    @DisplayName("노선의 제일 앞에 역을 추가한다")
    @Test
    void addStationStartOfLine() {
        //given
        final Sections sections = Sections.empty();
        registerStations("잠실역", "잠실새내역", "잠실나루역");
        sections.initializeSections("잠실역", "잠실나루역", 3);

        //when
        sections.addSection("잠실새내역", "잠실역", 4);

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(0).getUp()).isEqualTo(Station.get("잠실새내역")),
            () -> assertThat(sections.getSections().get(0).getDown()).isEqualTo(Station.get("잠실역")),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(4)
        );
    }

    @DisplayName("노선의 제일 뒤에 역을 추가한다")
    @Test
    void addStationEndOfLine() {
        //given
        final Sections sections = Sections.empty();
        registerStations("잠실역", "잠실새내역", "잠실나루역");
        sections.initializeSections("잠실나루역", "잠실역", 3);

        //when
        sections.addSection("잠실역", "잠실새내역", 4);

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(1).getUp()).isEqualTo(Station.get("잠실역")),
            () -> assertThat(sections.getSections().get(1).getDown()).isEqualTo(Station.get("잠실새내역")),
            () -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(4)
        );
    }

    @DisplayName("노선의 중간에 역을 추가한다 (나누어질 구간의 왼쪽으로 들어갈 떄)")
    @Test
    void addStationMiddleOfLineUpToDown() {
        //given
        final Sections sections = Sections.empty();
        registerStations("잠실역", "잠실새내역", "잠실나루역");
        sections.initializeSections("잠실나루역", "잠실새내역", 3);

        //when
        sections.addSection("잠실나루역", "잠실역", 2);

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(0).getUp()).isEqualTo(Station.get("잠실나루역")),
            () -> assertThat(sections.getSections().get(0).getDown()).isEqualTo(Station.get("잠실역")),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(2),
            () -> assertThat(sections.getSections().get(1).getUp()).isEqualTo(Station.get("잠실역")),
            () -> assertThat(sections.getSections().get(1).getDown()).isEqualTo(Station.get("잠실새내역")),
            () -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(1)
        );
    }

    @DisplayName("노선의 중간에 역을 추가한다 (나누어질 구간의 오른쪽으로 들어갈 떄)")
    @Test
    void addStationMiddleOfLineDownToUp() {
        //given
        final Sections sections = Sections.empty();
        registerStations("잠실역", "잠실새내역", "잠실나루역");
        sections.initializeSections("잠실나루역", "잠실새내역", 3);

        //when
        sections.addSection("잠실역", "잠실새내역", 2);

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(0).getUp()).isEqualTo(Station.get("잠실나루역")),
            () -> assertThat(sections.getSections().get(0).getDown()).isEqualTo(Station.get("잠실역")),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(1),
            () -> assertThat(sections.getSections().get(1).getUp()).isEqualTo(Station.get("잠실역")),
            () -> assertThat(sections.getSections().get(1).getDown()).isEqualTo(Station.get("잠실새내역")),
            () -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(2)
        );
    }


    @DisplayName("노선의 제일 앞에 있는 역을 제거한다.")
    @Test
    void removeStationStartOfLine() {
        //given
        final Sections sections = Sections.empty();
        registerStations("잠실역", "잠실새내역", "잠실나루역");
        sections.initializeSections("잠실나루역", "잠실역", 3);
        sections.addSection("잠실역", "잠실새내역", 2);

        //when
        sections.removeStation("잠실나루역");

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(1),
            () -> assertThat(sections.getSections().get(0).getUp()).isEqualTo(Station.get("잠실역")),
            () -> assertThat(sections.getSections().get(0).getDown()).isEqualTo(Station.get("잠실새내역")),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(2)
        );
    }

    @DisplayName("노선의 제일 뒤에 있는 역을 제거한다.")
    @Test
    void removeStationEndOfLine() {
        //given
        final Sections sections = Sections.empty();
        registerStations("잠실역", "잠실새내역", "잠실나루역");
        sections.initializeSections("잠실나루역", "잠실역", 3);
        sections.addSection("잠실역", "잠실새내역", 2);

        //when
        sections.removeStation("잠실새내역");

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(1),
            () -> assertThat(sections.getSections().get(0).getUp()).isEqualTo(Station.get("잠실나루역")),
            () -> assertThat(sections.getSections().get(0).getDown()).isEqualTo(Station.get("잠실역")),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(3)
        );
    }

    @DisplayName("노선의 중간에서 역을 제거한다.")
    @Test
    void removeStation() {
        //given
        final Sections sections = Sections.empty();
        registerStations("잠실역", "잠실새내역", "잠실나루역");
        sections.initializeSections("잠실나루역", "잠실역", 3);
        sections.addSection("잠실역", "잠실새내역", 2);

        //when
        sections.removeStation("잠실역");

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(1),
            () -> assertThat(sections.getSections().get(0).getUp()).isEqualTo(Station.get("잠실나루역")),
            () -> assertThat(sections.getSections().get(0).getDown()).isEqualTo(Station.get("잠실새내역")),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(5)
        );
    }
}
