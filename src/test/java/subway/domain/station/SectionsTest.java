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
        //given
        final Sections sections = new Sections();
        final Section section = new Section(
                new Station("from"),
                new Station("to"),
                new StationDistance(5)
        );
        //when
        sections.addInitialStations(section);

        //then
        assertThat(sections.getSections()).hasSize(1);
    }

    @Test
    void 역이_존재_하는_경우_최초_역_등록을_할_수_없다() {
        //given
        final Sections sections = new Sections();
        final Section section = new Section(
                new Station("from"),
                new Station("to"),
                new StationDistance(5)
        );
        //when
        sections.addInitialStations(section);

        //then
        assertThatThrownBy(() -> sections.addInitialStations(section))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 노선_역과_역_사이에_역_추가_테스트() {
        //given
        final Sections sections = new Sections();
        final Station startStation = new Station("start");
        final Section section = new Section(startStation, new Station("to"), new StationDistance(5));

        sections.addInitialStations(section);
        final Station newStation = new Station("newStation");

        //when
        sections.insertBehindStation(startStation, newStation, new StationDistance(3));

        //then
        assertThat(sections.getSections()).hasSize(2);
        assertThat(sections.peekByFirstStationUnique(startStation).getDistance())
                .isEqualTo(new StationDistance(3));
        assertThat(sections.peekByFirstStationUnique(newStation).getDistance())
                .isEqualTo(new StationDistance(2));
    }

    @Test
    void 구간의_시작_역으로_구간_조회_테스트() {
        //given
        final Sections sections = new Sections();
        final Station startStation = new Station("start");
        final Section initSection = new Section(startStation, new Station("to"), new StationDistance(5));
        sections.addInitialStations(initSection);

        //when
        final Section peekSection = sections.peekByFirstStationUnique(startStation);

        //then
        assertThat(peekSection).isEqualTo(initSection);
    }

    @Test
    void 구간의_끝_역으로_구간_조회_테스트() {
        //given
        final Sections sections = new Sections();
        final Station endStation = new Station("end");
        final Section initSection = new Section(new Station("from"), endStation, new StationDistance(5));
        sections.addInitialStations(initSection);

        //when
        final Section peekSection = sections.peekBySecondStationUnique(endStation);

        //then
        assertThat(peekSection).isEqualTo(initSection);
    }

    @Test
    void 노선_앞에_역_제거_테스트() {
        //given
        final Sections sections = new Sections();
        final Station startStation = new Station("start");
        final Section section = new Section(startStation, new Station("to"), new StationDistance(5));
        sections.addInitialStations(section);

        //when
        sections.removeFirstStation(startStation);

        //then
        assertThat(sections.getSections()).hasSize(0);
    }


    @Test
    void 노선_뒤에_역_제거_테스트() {
        //given
        final Sections sections = new Sections();
        final Station endStation = new Station("end");
        final Section section = new Section(new Station("from"), endStation, new StationDistance(5));

        sections.addInitialStations(section);

        //when
        sections.removeLastStation(endStation);

        //then
        assertThat(sections.getSections()).hasSize(0);
    }
}
