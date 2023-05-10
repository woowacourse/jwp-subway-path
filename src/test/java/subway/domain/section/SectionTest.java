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
        final boolean match = section.matchFirstStation(new Station("from"));

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
        final boolean match = section.matchSecondStation(new Station("to"));

        //then
        assertThat(match).isTrue();
    }

    @Test
    void 구간_첫번째역_기준으로_구간_생성() {
        //given
        final Section section = new Section(
                new Station("from"),
                new Station("to"),
                new StationDistance(5)
        );

        //when
        final Section frontSection =
                section.createFrontSection(new Station("fromFront"), new StationDistance(3));

        //then
        assertThat(frontSection.getSecondStation()).isEqualTo(new Station("from"));
    }

    @Test
    void 구간_두번째역_기준으로_구간_생성() {
        //given
        final Section section = new Section(
                new Station("from"),
                new Station("to"),
                new StationDistance(5)
        );

        //when
        final Section behindSection =
                section.createBehindSection(new Station("toBehind"), new StationDistance(3));

        //then
        assertThat(behindSection.getFirstStation()).isEqualTo(new Station("to"));
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
                section.separateByFirstStation(insertStation, new StationDistance(3));

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
    void createFrontSection() {
    }

    @Test
    void createBehindSection() {
    }

    @Test
    void createBetweenSections() {
    }
}
