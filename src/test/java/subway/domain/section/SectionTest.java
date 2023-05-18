package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;

@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void 첫번째역_동등성_테스트() {
        //given
        final Section section = new Section(
                new Station("from"),
                new Station("to"),
                new StationDistance(5)
        );

        //when
        final boolean match = section.matchFirstStationByName(new Station("from"));

        //then
        assertThat(match).isTrue();
    }

    @Test
    void 두번째역_동등성_테스트() {
        //given
        final Section section = new Section(
                new Station("from"),
                new Station("to"),
                new StationDistance(5)
        );

        //when
        final boolean match = section.matchSecondStationName(new Station("to"));

        //then
        assertThat(match).isTrue();
    }

    @Test
    void 역으로_구간_분리() {
        //given
        final Section section = new Section(
                new Station("from"),
                new Station("to"),
                new StationDistance(5)
        );

        //when
        final Station insertStation = new Station("between");
        final List<Section> separateSections =
                section.separateByInsertionStation(insertStation, new StationDistance(3));

        //then
        assertThat(separateSections).hasSize(2);
        final Section firstSection = separateSections.get(0);
        final Section secondSection = separateSections.get(1);
        assertThat(firstSection.getSecondStation()).isEqualTo(insertStation);
        assertThat(firstSection.getDistance()).isEqualTo(new StationDistance(3));
        assertThat(secondSection.getFirstStation()).isEqualTo(insertStation);
        assertThat(secondSection.getDistance()).isEqualTo(new StationDistance(2));
    }

    @Test
    void 구간_병합_테스트() {
        //given
        final Section sectionA = new Section(
                new Station("fromA"),
                new Station("toA"),
                new StationDistance(5)
        );

        final Section sectionB = new Section(
                new Station("fromB"),
                new Station("toB"),
                new StationDistance(3)
        );

        //when
        final Section mergedSection = sectionA.merge(sectionB);

        //then
        assertThat(mergedSection.getFirstStation()).isEqualTo(new Station("fromA"));
        assertThat(mergedSection.getSecondStation()).isEqualTo(new Station("toB"));
        assertThat(mergedSection.getDistance()).isEqualTo(new StationDistance(8));
    }
}
