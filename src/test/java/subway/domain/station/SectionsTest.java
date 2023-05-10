package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.domain.section.Sections;

@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {

    @Test
    void 최초_역_등록_테스트() {
        final Sections sections = new Sections();
        final Section section = new Section(
                new Station("from"),
                new Station("to"),
                new StationDistance(5)
        );

        sections.addInitialStations(section);

        assertThat(sections.getSections()).hasSize(1);
    }

    @Test
    void 역이_존재_하는_경우_최초_역_등록을_할_수_없다() {
        final Sections sections = new Sections();
        final Section section = new Section(
                new Station("from"),
                new Station("to"),
                new StationDistance(5)
        );

        sections.addInitialStations(section);

        assertThatThrownBy(() -> sections.addInitialStations(section))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 노선_앞에_역_추가_테스트() {
        //given
        final Sections sections = new Sections();
        final Station startStation = new Station("start");
        final Section section = new Section(startStation, new Station("to"), new StationDistance(5));

        sections.addInitialStations(section);
        final Station newStation = new Station("newStation");

        //when
        sections.addFirstStation(startStation, newStation, new StationDistance(3));

        //then
        assertThat(sections.getSections()).hasSize(2);
        assertThat(sections.peekUniqueSectionByFromStation(newStation).getDistance())
                .isEqualTo(new StationDistance(3));
    }
}
