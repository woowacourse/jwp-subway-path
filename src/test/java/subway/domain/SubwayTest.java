package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SubwayTest {
    private final Station station1 = new Station(1L, "station1");
    private final Station station2 = new Station(2L, "station2");
    private final Station station3 = new Station(3L, "station3");
    private final Station station4 = new Station(4L, "station4");
    private final Station station5 = new Station(5L, "station5");
    private final Station station6 = new Station(6L, "station6");


    @DisplayName("두 역의 최단 거리를 반환한다.")
    @Test
    void shortestDistanceBetween() {
        //given
        final Section section1 = new Section(station1, station2, new Distance(1));
        final Section section2 = new Section(station2, station3, new Distance(2));
        final Section section3 = new Section(station2, station4, new Distance(3));
        final Subway subway = new Subway(List.of(section1, section2, section3));

        //when
        final long distanceBetween1 = subway.shortestDistanceBetween(station1, station3);
        final long distanceBetween2 = subway.shortestDistanceBetween(station1, station4);

        //then
        assertAll(
                () -> assertThat(distanceBetween1).isEqualTo(3L),
                () -> assertThat(distanceBetween2).isEqualTo(4L)
        );
    }

    @DisplayName("두 역의 최단 경로를 반환한다.")
    @Test
    void shortestPathBetween() {
        //given
        final Section section1 = new Section(station1, station2, new Distance(1));
        final Section section2 = new Section(station2, station3, new Distance(2));
        final Section section3 = new Section(station2, station4, new Distance(3));
        final Subway subway = new Subway(List.of(section1, section2, section3));

        //when
        final List<Station> path1 = subway.shortestPathBetween(station1, station3);
        final List<Station> path2 = subway.shortestPathBetween(station1, station4);

        //then
        assertAll(
                () -> assertThat(path1).containsExactly(station1, station2, station3),
                () -> assertThat(path2).containsExactly(station1, station2, station4)
        );
    }
}
