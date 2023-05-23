package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static subway.integration.IntegrationFixture.*;

class SectionsTest {

    Sections sections;

    @BeforeEach
    void setUp() {
        final Section section1 = new Section(1L, STATION_A, STATION_B, DISTANCE5);
        final Section section2 = new Section(3L, STATION_C, STATION_D, DISTANCE5);
        final Section section3 = new Section(4L, STATION_B, STATION_C, DISTANCE5);
        sections = new Sections(List.of(section1, section2, section3));
    }

    @DisplayName("Sections 객체는 생성시 정렬된다.")
    @Test
    void sortTest() {
        assertThat(sections.getSections())
                .extracting(Section::getBeforeStation, Section::getNextStation)
                .containsExactly(
                        tuple(STATION_A, STATION_B),
                        tuple(STATION_B, STATION_C),
                        tuple(STATION_C, STATION_D)
                );
    }

    @DisplayName("Sections 객체는 상행 종점 삽입시 정렬된 상태를 유지한다.")
    @Test
    void Should_Sorted_When_Insert_Head_Section() {
        final Section newHeadSection = new Section(5L, STATION_E, STATION_A, DISTANCE5);
        final Sections addedSections = sections.addHead(newHeadSection);

        assertThat(addedSections.getSections())
                .extracting(Section::getBeforeStation, Section::getNextStation)
                .containsExactly(
                        tuple(STATION_E, STATION_A),
                        tuple(STATION_A, STATION_B),
                        tuple(STATION_B, STATION_C),
                        tuple(STATION_C, STATION_D)
                );
    }

    @DisplayName("Sections 객체는 중간 삽입시 정렬된 상태를 유지한다.")
    @Test
    void Should_Sorted_When_Insert_Central_Section() {
        final Section newHeadSection = new Section(5L, STATION_B, STATION_E, DISTANCE3);
        final Sections addedSections = sections.addCentral(newHeadSection);

        assertThat(addedSections.getSections())
                .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                .containsExactly(
                        tuple(STATION_A, STATION_B, DISTANCE5),
                        tuple(STATION_B, STATION_E, DISTANCE3),
                        tuple(STATION_E, STATION_C, DISTANCE2),
                        tuple(STATION_C, STATION_D, DISTANCE5)
                );
    }

    @DisplayName("Sections 객체는 하행 종점 삽입시 정렬된 상태를 유지한다.")
    @Test
    void Should_Sorted_When_Insert_Tail_Section() {
        final Section newHeadSection = new Section(5L, STATION_D, STATION_E, DISTANCE5);
        final Sections addedSections = sections.addTail(newHeadSection);

        assertThat(addedSections.getSections())
                .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                .containsExactly(
                        tuple(STATION_A, STATION_B, DISTANCE5),
                        tuple(STATION_B, STATION_C, DISTANCE5),
                        tuple(STATION_C, STATION_D, DISTANCE5),
                        tuple(STATION_D, STATION_E, DISTANCE5)
                );
    }

    @DisplayName("Sections 객체는 상행 종점 삭제시 정렬된 상태를 유지한다.")
    @Test
    void Should_Sorted_When_Delete_Head_Station() {
        final Sections removedSections = sections.removeHead();

        assertThat(removedSections.getSections())
                .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                .containsExactly(
                        tuple(STATION_B, STATION_C, DISTANCE5),
                        tuple(STATION_C, STATION_D, DISTANCE5)
                );
    }

    @DisplayName("Sections 객체는 중간 역 삭제시 정렬된 상태를 유지한다.")
    @Test
    void Should_Sorted_When_Delete_Central_Station() {
        final Sections removedSections = sections.removeCentral(STATION_C);

        assertThat(removedSections.getSections())
                .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                .containsExactly(
                        tuple(STATION_A, STATION_B, DISTANCE5),
                        tuple(STATION_B, STATION_D, DISTANCE10)
                );
    }

    @DisplayName("Sections 객체는 하행 종점 삭제시 정렬된 상태를 유지한다.")
    @Test
    void Should_Sorted_When_Delete_Tail_Station() {
        final Sections removedSections = sections.removeTail();

        assertThat(removedSections.getSections())
                .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                .containsExactly(
                        tuple(STATION_A, STATION_B, DISTANCE5),
                        tuple(STATION_B, STATION_C, DISTANCE5)
                );
    }

    @DisplayName("Sections 간의 차집합을 구한다.")
    @Test
    void getDifferenceOfSet() {
        final Station stationA = new Station(1L, new StationName("A"));
        final Station stationB = new Station(2L, new StationName("B"));
        final Station stationC = new Station(3L, new StationName("C"));
        final Section sections1 = new Section(1L, stationA, stationB, new Distance(3));
        final Section sections2 = new Section(2L, stationB, stationC, new Distance(4));
        final Sections originSections = new Sections(List.of(sections1, sections2));
        final Sections newSections = originSections.removeHead();

        final Sections difference = originSections.getDifferenceOfSet(newSections);

        assertThat(difference.getSections())
                .containsExactly(sections1);
    }
}
