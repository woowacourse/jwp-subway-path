package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.Fixture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


class SectionMapTest {

    @Test
    @DisplayName("SectionMap 생성 시 상행에서 하행 순서로 정렬된다.")
    void sortSectionMap() {
        // given
        final Map<Station, Section> testSectionMap = new HashMap<>();
        testSectionMap.put(Fixture.stationB, Fixture.sectionBC);
        testSectionMap.put(Fixture.stationC, Fixture.sectionCD);
        testSectionMap.put(Fixture.stationA, Fixture.sectionAB);

        // when
        final SectionMap sectionMap = new SectionMap(testSectionMap, Fixture.stationA);

        // then
        final List<Station> stations = new ArrayList<>(sectionMap.getSectionMap().keySet());
        assertThat(stations).containsExactly(Fixture.stationA, Fixture.stationB, Fixture.stationC);
    }

    @Test
    @DisplayName("첫 역 및 구간 추가")
    void addInitialSection() {
        // given
        final SectionMap sectionMap = new SectionMap();

        // when
        sectionMap.addInitialSection(Fixture.stationA, Fixture.stationB, 1);

        // then
        final Map<Station, Section> actual = sectionMap.getSectionMap();
        assertAll(
                () -> assertThat(actual.keySet().size()).isEqualTo(1),
                () -> assertThat(actual.get(Fixture.stationA)).isEqualTo(Fixture.sectionAB)
        );
    }

    @Test
    @DisplayName("역이 있는 노선에 초기역을 추가하려하면 예외 발생")
    void addInitialSectionException() {
        // given
        final Map<Station, Section> testMap = new HashMap<>();
        testMap.put(Fixture.stationB, Fixture.sectionBC);
        final SectionMap sectionMap = new SectionMap(testMap, Fixture.stationB);

        // when & then
        assertThatThrownBy(() -> sectionMap.addInitialSection(Fixture.stationA, Fixture.stationC, 15)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 노선에 중간역을 추가하려하면 예외 발생")
    void addSectionEmptyException() {
        // given
        final SectionMap sectionMap = new SectionMap();

        // when & then
        assertThatThrownBy(() -> sectionMap.addSection(Fixture.stationA, Fixture.stationC, 15)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상행 종점 추가")
    void addSectionUpEndstation() {
        // given
        final Map<Station, Section> testMap = new HashMap<>();
        testMap.put(Fixture.stationB, Fixture.sectionBC);
        final SectionMap sectionMap = new SectionMap(testMap, Fixture.stationB);

        // when
        final Section addedSection = sectionMap.addSection(Fixture.stationA, Fixture.stationB, 1);

        // then
        final Map<Station, Section> actual = sectionMap.getSectionMap();
        assertAll(
                () -> assertThat(addedSection).isEqualTo(Fixture.sectionAB),
                () -> assertThat(actual.keySet().size()).isEqualTo(2),
                () -> assertThat(actual.get(Fixture.stationA)).isEqualTo(Fixture.sectionAB),
                () -> assertThat(actual.get(Fixture.stationB)).isEqualTo(Fixture.sectionBC)
        );
    }

    @Test
    @DisplayName("하행 종점 추가")
    void addSectionDownEndstation() {
        // given
        final Map<Station, Section> testMap = new HashMap<>();
        testMap.put(Fixture.stationB, Fixture.sectionBC);
        final SectionMap sectionMap = new SectionMap(testMap, Fixture.stationB);

        // when
        final Section addedSection = sectionMap.addSection(Fixture.stationC, Fixture.stationD, 1);

        // then
        final Map<Station, Section> actual = sectionMap.getSectionMap();
        assertAll(
                () -> assertThat(addedSection).isEqualTo(Fixture.sectionCD),
                () -> assertThat(actual.keySet().size()).isEqualTo(2),
                () -> assertThat(actual.get(Fixture.stationB)).isEqualTo(Fixture.sectionBC),
                () -> assertThat(actual.get(Fixture.stationC)).isEqualTo(Fixture.sectionCD)
        );
    }

    @Test
    @DisplayName("상행 역 기반 중간역 추가")
    void addSectionMidBasedUp() {
        // given
        final Map<Station, Section> testMap = new HashMap<>();
        testMap.put(Fixture.stationA, Fixture.sectionAB);
        final SectionMap sectionMap = new SectionMap(testMap, Fixture.stationA);
        final Section sectionAC = new Section(Fixture.stationA, Fixture.stationC, 5);
        final Section sectionCB = new Section(Fixture.stationC, Fixture.stationB, 5);

        // when
        final Section addedSection = sectionMap.addSection(Fixture.stationA, Fixture.stationC, 5);

        // then
        final Map<Station, Section> actual = sectionMap.getSectionMap();

        assertAll(
                () -> assertThat(addedSection).isEqualTo(sectionAC),
                () -> assertThat(actual.keySet().size()).isEqualTo(2),
                () -> assertThat(actual.get(Fixture.stationA)).isEqualTo(sectionAC),
                () -> assertThat(actual.get(Fixture.stationC)).isEqualTo(sectionCB)
        );
    }

    @Test
    @DisplayName("하행 역 기반 중간역 추가")
    void addSectionMidBasedDown() {
        // given
        final Map<Station, Section> testMap = new HashMap<>();
        testMap.put(Fixture.stationA, Fixture.sectionAB);
        final SectionMap sectionMap = new SectionMap(testMap, Fixture.stationA);
        final Section sectionAC = new Section(Fixture.stationA, Fixture.stationC, 5);
        final Section sectionCB = new Section(Fixture.stationC, Fixture.stationB, 5);

        // when
        final Section addedSection = sectionMap.addSection(Fixture.stationC, Fixture.stationB, 5);

        // then
        final Map<Station, Section> actual = sectionMap.getSectionMap();

        assertAll(
                () -> assertThat(addedSection).isEqualTo(sectionCB),
                () -> assertThat(actual.keySet().size()).isEqualTo(2),
                () -> assertThat(actual.get(Fixture.stationA)).isEqualTo(sectionAC),
                () -> assertThat(actual.get(Fixture.stationC)).isEqualTo(sectionCB)
        );
    }

    @Test
    @DisplayName("역 추가시 이미 역이 존재하는 경우 예외 발생")
    void addSectionAlreadyExistException() {
        // given
        final Map<Station, Section> testMap = new HashMap<>();
        testMap.put(Fixture.stationB, Fixture.sectionBC);
        final SectionMap sectionMap = new SectionMap(testMap, Fixture.stationB);

        // when & then
        assertThatThrownBy(() -> sectionMap.addSection(Fixture.stationB, Fixture.stationC, 1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("역 추가시 기반이 될 역이 존재한지 않으면 예외 발생")
    void addSectionNotExistException() {
        // given
        final Map<Station, Section> testMap = new HashMap<>();
        testMap.put(Fixture.stationB, Fixture.sectionBC);
        final SectionMap sectionMap = new SectionMap(testMap, Fixture.stationB);

        // when & then
        assertThatThrownBy(() -> sectionMap.addSection(Fixture.stationA, Fixture.stationD, 1)).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("역 추가시 길이가 적절하지 않으면 예외 발생")
    void addSectionDistanceException() {
        // given
        final Map<Station, Section> testMap = new HashMap<>();
        testMap.put(Fixture.stationA, Fixture.sectionAB);
        final SectionMap sectionMap = new SectionMap(testMap, Fixture.stationA);

        // when & then
        assertThatThrownBy(() -> sectionMap.addSection(Fixture.stationA, Fixture.stationC, 15)).isInstanceOf(IllegalArgumentException.class);

    }
}
