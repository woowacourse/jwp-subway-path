package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private static final Station STATION_A = new Station(1L, "A");
    private static final Station STATION_B = new Station(2L, "B");
    private static final Station STATION_C = new Station(3L, "C");
    private static final Section SECTIONS_1 = new Section(1L, STATION_A, STATION_B, new Distance(3));
    private static final Section SECTIONS_2 = new Section(2L, STATION_B, STATION_C, new Distance(4));
    private static final Sections ORIGIN_SECTIONS = new Sections(List.of(SECTIONS_1, SECTIONS_2));

    @DisplayName("Sections 간의 차집합을 구한다.")
    @Test
    void getDifferenceOfSet() {
        final Sections newSections = ORIGIN_SECTIONS.removeHead();

        final Sections difference = ORIGIN_SECTIONS.getDifferenceOfSet(newSections);

        assertThat(difference.getSections())
                .containsExactly(SECTIONS_1);
    }

    @DisplayName("Sections에서 해당 Station이 상행종점인지 확인한다.")
    @Test
    void isHeadStation() {
        assertAll(
                () -> assertThat(ORIGIN_SECTIONS.isHeadStation(STATION_A)).isTrue(),
                () -> assertThat(ORIGIN_SECTIONS.isHeadStation(STATION_B)).isFalse()
        );
    }

    @DisplayName("Sections에서 해당 Station이 하행종점인지 확인한다.")
    @Test
    void isTailStation() {
        assertAll(
                () -> assertThat(ORIGIN_SECTIONS.isTailStation(STATION_C)).isTrue(),
                () -> assertThat(ORIGIN_SECTIONS.isTailStation(STATION_B)).isFalse()
        );
    }

    @DisplayName("Sections의 상행종점에 section을 추가한다.")
    @Test
    void addHead() {
        final Station stationD = new Station(4L, "D");
        final Section newSection = new Section(stationD, STATION_A, new Distance(10));

        final Sections addedSections = ORIGIN_SECTIONS.addHead(newSection);

        assertThat(addedSections.getSections())
                .containsExactly(newSection, SECTIONS_1, SECTIONS_2);
    }

    @DisplayName("Sections의 상행종점에 section을 추가한다.")
    @Test
    void addTail() {
        final Station stationD = new Station(4L, "D");
        final Section newSection = new Section(STATION_C, stationD, new Distance(10));

        final Sections addedSections = ORIGIN_SECTIONS.addTail(newSection);

        assertThat(addedSections.getSections())
                .containsExactly(SECTIONS_1, SECTIONS_2, newSection);
    }
}
