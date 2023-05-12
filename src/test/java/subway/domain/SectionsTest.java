package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    @DisplayName("sections들을 정렬한다.")
    void testSort() {
        //given
        final Station topStation = new Station(1L, "topStation");
        final Station midUpStation = new Station(2L, "midUpStation");
        final Station midDownStation = new Station(2L, "midDownStation");
        final Station bottomStation = new Station(3L, "bottomStation");
        final long distance = 10L;
        final Section topSection = new Section(1L, topStation, midUpStation, distance);
        final Section midSection = new Section(2L, midUpStation, midDownStation, distance);
        final Section bottomSection = new Section(2L, midDownStation, bottomStation, distance);
        final Sections sections = new Sections(new ArrayList<>(List.of(bottomSection, topSection, midSection)));

        //when
        sections.sort();

        //then
        assertThat(sections.findSection(0)).isEqualTo(topSection);
        assertThat(sections.findSection(1)).isEqualTo(midSection);
        assertThat(sections.findSection(2)).isEqualTo(bottomSection);
    }

    @Test
    @DisplayName("station으로 section을 찾아온다.")
    void testFindSection() {
        //given
        final Station topStation = new Station(1L, "topStation");
        final Station midUpStation = new Station(2L, "midUpStation");
        final Station midDownStation = new Station(2L, "midDownStation");
        final Station bottomStation = new Station(3L, "bottomStation");
        final long distance = 10L;
        final Section topSection = new Section(1L, topStation, midUpStation, distance);
        final Section midSection = new Section(2L, midUpStation, midDownStation, distance);
        final Section bottomSection = new Section(2L, midDownStation, bottomStation, distance);
        final Sections sections = new Sections(new ArrayList<>(List.of(bottomSection, topSection, midSection)));

        //when
        final Section result = sections.findSection(midUpStation, midDownStation);

        //then
        assertThat(result).isEqualTo(midSection);
    }
}
