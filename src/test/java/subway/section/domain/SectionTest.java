package subway.section.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.station.domain.Station;

class SectionTest {

    private static final Station 잠실역 = new Station(1L, "잠실역");
    private static final Station 잠실새내역 = new Station(2L, "잠실새내역");

    @DisplayName("새로운 구간을 등록한다.")
    @Test
    void of() {
        //given
        //when
        //then
        Assertions.assertDoesNotThrow(() -> Section.of(잠실역, 잠실새내역, 3));
    }

    @DisplayName("해당역에서 시작하는지 확인한다.")
    @Test
    void isStartWith() {
        //given
        //when
        final Section section = Section.of(잠실역, 잠실새내역, 3);

        //then
        assertThat(section.isStartWith(잠실역)).isTrue();
    }

    @DisplayName("해당역에서 끝나는지 확인한다.")
    @Test
    void isEndWith() {
        //given
        //when
        final Section section = Section.of(잠실역, 잠실새내역, 3);

        //then
        assertThat(section.isEndWith(잠실새내역)).isTrue();
    }

    @DisplayName("해당역을 포함하는지 확인한다.")
    @Test
    void isContainStation() {
        //given
        //when
        final Section section = Section.of(잠실역, 잠실새내역, 3);

        //then
        assertThat(section.isContainStation(잠실역)).isTrue();
        assertThat(section.isContainStation(잠실새내역)).isTrue();
    }
}
