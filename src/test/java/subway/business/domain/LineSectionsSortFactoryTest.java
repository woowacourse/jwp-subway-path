package subway.business.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineSectionsSortFactoryTest {

    @Test
    @DisplayName("정상적으로 정렬되는지 확인")
    void sort() {
        // given
        final Line line = new Line(1L, "2호선", "bg-green-600");
        final Distance distance = new Distance(1);

        final Station station1 = new Station(1L, "잠실");
        final Station station2 = new Station(2L, "잠실새내");
        final Station station3 = new Station(3L, "종합운동장");
        final Station station4 = new Station(4L, "삼성");
        final Station station5 = new Station(5L, "선릉");
        final Station station6 = new Station(6L, "역삼");
        final Station station7 = new Station(7L, "강남");
        final Station station8 = new Station(8L, "교대");

        final Section section1 = new Section(1L, line, station3, station4, distance);
        final Section section2 = new Section(2L, line, station6, station7, distance);
        final Section section3 = new Section(3L, line, station1, station2, distance);
        final Section section4 = new Section(4L, line, station7, station8, distance);
        final Section section5 = new Section(5L, line, station4, station5, distance);
        final Section section6 = new Section(6L, line, station2, station3, distance);
        final Section section7 = new Section(7L, line, station5, station6, distance);

        final List<Section> unsortedSections = List.of(section1, section2, section3, section4, section5, section6, section7);

        // when
        final List<Section> sortedSections = LineSectionsSortFactory.sort(unsortedSections);

        // then
        assertAll(
                () -> assertThat(sortedSections.get(0).getId()).isEqualTo(3L),
                () -> assertThat(sortedSections.get(1).getId()).isEqualTo(6L),
                () -> assertThat(sortedSections.get(2).getId()).isEqualTo(1L),
                () -> assertThat(sortedSections.get(3).getId()).isEqualTo(5L),
                () -> assertThat(sortedSections.get(4).getId()).isEqualTo(7L),
                () -> assertThat(sortedSections.get(5).getId()).isEqualTo(2L),
                () -> assertThat(sortedSections.get(6).getId()).isEqualTo(4L)
        );

    }
}
