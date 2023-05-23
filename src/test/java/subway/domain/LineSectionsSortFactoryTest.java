package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.LineSection;
import subway.domain.LineSectionsSortFactory;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineSectionsSortFactoryTest {

    @Test
    @DisplayName("정상적으로 정렬되는지 확인")
    void sort() {
        // given
        final Distance distance = new Distance(1);

        final Station station1 = new Station(1L, "잠실");
        final Station station2 = new Station(2L, "잠실새내");
        final Station station3 = new Station(3L, "종합운동장");
        final Station station4 = new Station(4L, "삼성");
        final Station station5 = new Station(5L, "선릉");
        final Station station6 = new Station(6L, "역삼");
        final Station station7 = new Station(7L, "강남");
        final Station station8 = new Station(8L, "교대");

        final LineSection section1 = new LineSection(station3, station4, distance);
        final LineSection section2 = new LineSection(station6, station7, distance);
        final LineSection section3 = new LineSection(station1, station2, distance);
        final LineSection section4 = new LineSection(station7, station8, distance);
        final LineSection section5 = new LineSection(station4, station5, distance);
        final LineSection section6 = new LineSection(station2, station3, distance);
        final LineSection section7 = new LineSection(station5, station6, distance);

        final List<LineSection> unsortedSections = List.of(section1, section2, section3, section4, section5, section6, section7);

        // when
        final List<LineSection> sortedSections = LineSectionsSortFactory.sort(unsortedSections);

        // then
        assertAll(
                () -> assertThat(sortedSections.get(0).getPreviousStation().getName()).isEqualTo("잠실"),
                () -> assertThat(sortedSections.get(0).getNextStation().getName()).isEqualTo("잠실새내"),
                () -> assertThat(sortedSections.get(1).getNextStation().getName()).isEqualTo("종합운동장"),
                () -> assertThat(sortedSections.get(2).getNextStation().getName()).isEqualTo("삼성"),
                () -> assertThat(sortedSections.get(3).getNextStation().getName()).isEqualTo("선릉"),
                () -> assertThat(sortedSections.get(4).getNextStation().getName()).isEqualTo("역삼"),
                () -> assertThat(sortedSections.get(5).getNextStation().getName()).isEqualTo("강남"),
                () -> assertThat(sortedSections.get(6).getNextStation().getName()).isEqualTo("교대")
        );

    }
}
