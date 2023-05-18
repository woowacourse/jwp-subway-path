package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.Station;

class SectionsTest {

    private static final Station STATION_A = new Station(1L, "A");
    private static final Station STATION_B = new Station(2L, "B");
    private static final Station STATION_C = new Station(3L, "C");
    private static final Section SECTION_1 = new Section(1L, STATION_A, STATION_B, new Distance(3));
    private static final Section SECTION_2 = new Section(2L, STATION_B, STATION_C, new Distance(4));
    private static final Sections ORIGIN_FILLED_SECTIONS = new EmptySections()
            .addSection(SECTION_1)
            .addSection(SECTION_2);

    @DisplayName("Sections 간의 차집합을 구한다.")
    @Test
    void getDifferenceOfSet() {
        final Sections newSections = ORIGIN_FILLED_SECTIONS.removeStation(STATION_C);

        final Sections difference = ORIGIN_FILLED_SECTIONS.getDifferenceOfSet(newSections);

        assertThat(difference.getSections())
                .containsExactly(SECTION_2);
    }

    @DisplayName("Sections간의 차집합을 구하는데, 차집합이 없는 경우")
    @Test
    void getDifferenceOfSethNoDifference() {
        final Sections emptySections = ORIGIN_FILLED_SECTIONS.getDifferenceOfSet(ORIGIN_FILLED_SECTIONS);

        assertThat(emptySections.getSections()).isEmpty();
    }

    @DisplayName("Sections의 상행종점에 section을 추가한다.")
    @Test
    void addHead() {
        final Station stationD = new Station(4L, "D");
        final Section newSection = new Section(stationD, STATION_A, new Distance(10));

        final Sections addedFilledSections = ORIGIN_FILLED_SECTIONS.addSection(newSection);

        assertThat(addedFilledSections.getSections())
                .containsExactly(newSection, SECTION_1, SECTION_2);
    }

    @DisplayName("Sections의 하행종점에 section을 추가한다.")
    @Test
    void addTail() {
        final Station stationD = new Station(4L, "D");
        final Section newSection = new Section(STATION_C, stationD, new Distance(10));

        final Sections addedFilledSections = ORIGIN_FILLED_SECTIONS.addSection(newSection);

        assertThat(addedFilledSections.getSections())
                .containsExactly(SECTION_1, SECTION_2, newSection);
    }

    @DisplayName("Sections 중간에 Section을 추가한다.")
    @Nested
    class addCentral {

        @DisplayName("Sections의 중간에 section을 추가하되, 요청과 일치하는 역은 prevStation이다.")
        @Test
        void addCentralPrevEqualCase() {
            final Station stationD = new Station(4L, "D");
            final Section newSection = new Section(STATION_A, stationD, new Distance(1));

            final Sections addedFilledSections = ORIGIN_FILLED_SECTIONS.addSection(newSection);

            assertThat(addedFilledSections.getSections())
                    .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(STATION_A, stationD, new Distance(1)),
                            tuple(stationD, STATION_B, new Distance(2)),
                            tuple(STATION_B, STATION_C, new Distance(4))
                    );
        }

        @DisplayName("Sections의 중간에 section을 추가하되, 요청과 일치는 역은 nextStation이다.")
        @Test
        void addCentralNextEqualCase() {
            final Station stationD = new Station(4L, "D");
            final Section newSection = new Section(stationD, STATION_B, new Distance(1));

            final Sections addedFilledSections = ORIGIN_FILLED_SECTIONS.addSection(newSection);

            assertThat(addedFilledSections.getSections())
                    .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(STATION_A, stationD, new Distance(2)),
                            tuple(stationD, STATION_B, new Distance(1)),
                            tuple(STATION_B, STATION_C, new Distance(4))
                    );
        }

    }

    @DisplayName("상행종점을 제거한다.")
    @Test
    void removeHead() {
        final Sections filledSections = ORIGIN_FILLED_SECTIONS.removeStation(STATION_A);

        assertThat(filledSections.getSections())
                .containsExactly(SECTION_2);
    }

    @DisplayName("하행종점을 제거한다.")
    @Test
    void removeTail() {
        final Sections filledSections = ORIGIN_FILLED_SECTIONS.removeStation(STATION_C);

        assertThat(filledSections.getSections())
                .containsExactly(SECTION_1);
    }

    @DisplayName("중간에 있는 역을 제거한다.")
    @Test
    void removeCentral() {
        final Sections filledSections = ORIGIN_FILLED_SECTIONS.removeStation(STATION_B);

        assertThat(filledSections.getSections())
                .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                .containsExactly(tuple(STATION_A, STATION_C, new Distance(7)));
    }

    @DisplayName("존재하는 모든 Station들을 반환한다.")
    @Test
    void getAllStations() {
        final List<Station> allStations = ORIGIN_FILLED_SECTIONS.getAllStations();

        assertThat(allStations)
                .containsExactly(STATION_A, STATION_B, STATION_C);
    }
}
