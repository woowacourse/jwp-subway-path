package subway.subwayMap.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.domain.Section;
import subway.domain.station.domain.Station;
import subway.domain.subwayMap.domain.SectionToStationConverter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionToStationConverterTest {

    @DisplayName("노선의 경로 조회 테스트")
    @Test
    void getStations() {
        final Station station1 = new Station(1L, "1L");
        final Station station2 = new Station(2L, "2L");
        final Station station3 = new Station(3L, "3L");
        final Station station4 = new Station(4L, "4L");
        final Station station5 = new Station(5L, "5L");
        final Station station6 = new Station(6L, "6L");
        final Station station7 = new Station(7L, "7L");

        final List<Section> sections = List.of(
                new Section(1L, 1L, station1, station2, 3),
                new Section(2L, 1L, station2, station3, 3),
                new Section(3L, 1L, station3, station4, 3),
                new Section(4L, 1L, station4, station5, 3),
                new Section(5L, 1L, station5, station6, 3),
                new Section(6L, 1L, station6, station7, 3)
        );

        final SectionToStationConverter sectionToStationConverter = SectionToStationConverter.of(sections);
        assertThat(sectionToStationConverter.getSortedStation()).containsExactly(
                station1,
                station2,
                station3,
                station4,
                station5,
                station6,
                station7
        );
    }

    @DisplayName("노선의 경로 조회 테스트")
    @Test
    void getStations2() {
        final Station station5 = new Station(5L, "5L");
        final Station station6 = new Station(6L, "6L");
        final Station station7 = new Station(7L, "7L");
        final Station station1 = new Station(1L, "1L");
        final Station station2 = new Station(2L, "2L");
        final Station station3 = new Station(3L, "3L");
        final Station station4 = new Station(4L, "4L");

        final List<Section> sections = List.of(
                new Section(1L, 1L, station1, station2, 3),
                new Section(2L, 1L, station2, station3, 3),
                new Section(3L, 1L, station3, station4, 3),
                new Section(4L, 1L, station4, station5, 3),
                new Section(5L, 1L, station5, station6, 3),
                new Section(6L, 1L, station6, station7, 3)
        );

        final SectionToStationConverter sectionToStationConverter = SectionToStationConverter.of(sections);
        assertThat(sectionToStationConverter.getSortedStation()).containsExactly(
                station1,
                station2,
                station3,
                station4,
                station5,
                station6,
                station7
        );
    }
}
