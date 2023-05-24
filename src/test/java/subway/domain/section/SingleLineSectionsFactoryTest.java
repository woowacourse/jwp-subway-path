package subway.domain.section;

import org.junit.jupiter.api.Test;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class SingleLineSectionsFactoryTest {

    @Test
    void 구간이_순서대로_생성된다() {
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
        final List<Section> sections = List.of(section1, section2, section3, section4);

        // when
        final SingleLineSections sortedSections = SectionsFactory.createSortedSections(sections);

        // then
        assertThat(sortedSections.getSections()).containsExactly(section3, section4, section1, section2);
    }

}
