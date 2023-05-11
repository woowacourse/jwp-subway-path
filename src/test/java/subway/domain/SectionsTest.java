package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @DisplayName("Sections 간의 차집합을 구한다.")
    @Test
    void getDifferenceOfSet() {
        final Station stationA = new Station(1L, "A");
        final Station stationB = new Station(2L, "B");
        final Station stationC = new Station(3L, "C");
        final Section sections1 = new Section(1L, stationA, stationB, new Distance(3));
        final Section sections2 = new Section(2L, stationB, stationC, new Distance(4));
        final Sections originSections = new Sections(List.of(sections1, sections2));
        final Sections newSections = originSections.removeHead();

        final Sections difference = originSections.getDifferenceOfSet(newSections);

        assertThat(difference.getSections())
                .containsExactly(sections1);
    }
}
