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
        final Section topSection = new Section(1L, topStation, midUpStation);
        final Section midSection = new Section(1L, midUpStation, midDownStation);
        final Section bottomSection = new Section(2L, midDownStation, bottomStation);
        final Sections sections = new Sections(new ArrayList<>(List.of(bottomSection, topSection, midSection)));

        //when
        sections.sort();

        //then
        assertThat(sections.getSection(0)).isEqualTo(topSection);
        assertThat(sections.getSection(1)).isEqualTo(midSection);
        assertThat(sections.getSection(2)).isEqualTo(bottomSection);
    }
}
