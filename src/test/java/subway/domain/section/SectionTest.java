package subway.domain.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.exception.CannotLinkException;
import subway.exception.DuplicateSectionException;
import subway.exception.LineNotFoundException;

import static fixtures.LineFixtures.LINE2;
import static fixtures.SectionFixtures.*;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {

    @Test
    @DisplayName("해당 섹션이 어느 노선에 있는지 반환한다.")
    void getLineTest_success() {
        // given
        Section section = SECTION_잠실역_TO_건대역;
        Line expectLine = LINE2;

        // when
        Line line = section.getLine();

        // then
        assertThat(line).isEqualTo(expectLine);
    }

    @Test
    @DisplayName("해당 섹션을 구성하는 상행역과 하행역이 포함된 노선이 서로 다르면 예외를 반환한다.")
    void getLineTest_fail() {
        // given
        Section section = new Section(null, STATION_잠실역, STATION_온수역, 10);

        // when, then
        assertThatThrownBy(() -> section.getLine())
                .isInstanceOf(LineNotFoundException.class)
                .hasMessage("노선을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("현재 섹션에서 다른 섹션을 빼고난 후의 섹션을 구한다.")
    void subtractSectionTest_success() {
        // given
        Section currentSection = SECTION_잠실역_TO_건대역;
        Section sectionToSubtract = SECTION_TO_INSERT_강변역_TO_건대역;
        Section expectSection = SECTION_AFTER_CALCULATE_잠실역_TO_강변역;

        // when
        Section sectionAfterSubtract = currentSection.subtract(sectionToSubtract);

        // then
        assertThat(sectionAfterSubtract).isEqualTo(expectSection);
    }

    @Test
    @DisplayName("현재 섹션과 동일한 섹션을 빼려고 하면 예외가 발생한다.")
    void subtractSectionTest_fail_sameSection() {
        // given
        Section currentSection = SECTION_잠실역_TO_건대역;
        Section sectionToSubtract = SECTION_TO_INSERT_잠실역_TO_건대역;

        // when, then
        assertThatThrownBy(() -> currentSection.subtract(sectionToSubtract))
                .isInstanceOf(DuplicateSectionException.class)
                .hasMessage("이미 포함되어 있는 구간입니다.");
    }

    @Test
    @DisplayName("현재 섹션과 연결점이 없는 섹션을 합치려고 하면 에외가 발생한다.")
    void combineSectionTest_fail_cannotLink() {
        // given
        Section currentSection = SECTION_잠실역_TO_건대역;
        Section sectionToSubtract = SECTION_TO_INSERT_대림역_TO_신림역;

        // when, then
        assertThatThrownBy(() -> currentSection.combine(sectionToSubtract))
                .isInstanceOf(CannotLinkException.class)
                .hasMessage("현재 등록된 역 중에 하나를 포함해야합니다.");
    }

    @Test
    @DisplayName("해당 섹션과 연결된 역이 없다면 예외가 발생한다.")
    void subtractSectionTest_fail_cannotLink() {
        // given
        Section currentSection = SECTION_잠실역_TO_건대역;
        Section sectionToSubtract = SECTION_TO_INSERT_대림역_TO_신림역;

        // when, then
        assertThatThrownBy(() -> currentSection.subtract(sectionToSubtract))
                .isInstanceOf(CannotLinkException.class)
                .hasMessage("현재 등록된 역 중에 하나를 포함해야합니다.");
    }

    @Test
    @DisplayName("현재 섹션 안에 주어진 섹션을 포함시킬 수 있다면 true를 반환한다.")
    void isContainSectionTest_true() {
        // given
        Section section = SECTION_잠실역_TO_건대역;
        Section targetSection = SECTION_TO_INSERT_잠실역_TO_강변역;

        // when
        boolean isContainSection = section.isContainSection(targetSection);

        // then
        assertThat(isContainSection).isTrue();
    }

    @Test
    @DisplayName("현재 섹션 안에 주어진 섹션을 포함시킬 수 없다면 false를 반환한다.")
    void isContainSectionTest_false() {
        // given
        Section section = SECTION_잠실역_TO_건대역;
        Section targetSection = SECTION_TO_INSERT_대림역_TO_신림역;

        // when
        boolean isContainSection = section.isContainSection(targetSection);

        // then
        assertThat(isContainSection).isFalse();
    }

    @Test
    @DisplayName("현재 섹션에 주어진 역이 포함되어 있다면 true를 반환한다.")
    void isContainStationTest_true() {
        // given
        Section section = SECTION_잠실역_TO_건대역;
        Station targetStation = STATION_건대역;

        // when
        boolean isContainStation = section.isContainStation(targetStation);

        // then
        assertThat(isContainStation).isTrue();
    }

    @Test
    @DisplayName("현재 섹션에 주어진 역이 포함되어 있지 않다면 false를 반환한다.")
    void isContainStationTest_false() {
        // given
        Section section = SECTION_잠실역_TO_건대역;
        Station targetStation = STATION_강변역;

        // when
        boolean isContainStation = section.isContainStation(targetStation);

        // then
        assertThat(isContainStation).isFalse();
    }
}