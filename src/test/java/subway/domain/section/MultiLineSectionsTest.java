package subway.domain.section;

import org.junit.jupiter.api.Test;
import subway.domain.Station;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
class MultiLineSectionsTest {

    @Test
    void 포함된_모든_역들을_반환한다() {
        // given
        final Station 잠실역 = new Station(1L, "잠실역");
        final Station 잠실새내역 = new Station(2L, "잠실새내역");
        final Station 종합운동장역 = new Station(3L, "종합운동장역");
        final Station 삼성역 = new Station(4L, "삼성역");
        final Station 선릉역 = new Station(5L, "선릉역");

        final Section section1 = new Section(10, 종합운동장역, 삼성역, 1L);
        final Section section2 = new Section(10, 삼성역, 선릉역, 1L);
        final Section section3 = new Section(10, 잠실역, 잠실새내역, 1L);
        final Section section4 = new Section(10, 잠실새내역, 종합운동장역, 1L);

        final MultiLineSections sections = MultiLineSections.from(List.of(section1, section2, section3, section4));

        // when
        final List<Station> allStations = sections.getAllStations();

        // then
        final List<String> stationNames = allStations.stream().map(Station::getName).collect(toList());
        assertAll(
                () -> assertThat(allStations).hasSize(5),
                () -> assertThat(stationNames).containsExactlyInAnyOrder("잠실역", "잠실새내역", "종합운동장역", "삼성역", "선릉역")
        );
    }
}
