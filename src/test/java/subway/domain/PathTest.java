package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.vo.Distance;

class PathTest {

    private final Station firstStation = new Station("firstStation");
    private final Station secondStation = new Station("secondStation");
    private final Station thirdStation = new Station("thirdStation");
    private final Station fourthStation = new Station("fourthStation");
    private final Section section1 = new Section(firstStation, secondStation, new Distance(1L));
    private final Section section2 = new Section(secondStation, thirdStation, new Distance(1L));
    private final Section section3 = new Section(thirdStation, fourthStation, new Distance(1L));
    private final Section section4 = new Section(secondStation, firstStation, new Distance(10L));
    private final Section section5 = new Section(firstStation, thirdStation, new Distance(11L));
    private final Section section6 = new Section(thirdStation, fourthStation, new Distance(12L));


    @Test
    @DisplayName("경로들 중 최단 경로를 찾는다.")
    void testFindShortest() {
        //given
        final Sections sections1 = new Sections(List.of(section1, section2, section3));
        final Line line1 = new Line("lineName", "lineColor", sections1);
        final Sections sections2 = new Sections(List.of(section4, section5, section6));
        final Line line2 = new Line("lineName", "lineColor", sections2);
        final Lines lines = new Lines(List.of(line1, line2));
        final Path path = new Path(lines);

        //when
        final List<Station> shortest = path.findShortest(firstStation, fourthStation);

        //then
        assertThat(shortest).containsExactly(firstStation, secondStation, thirdStation, fourthStation);
    }

    @Test
    @DisplayName("경로들 중 최단 경로의 총 거리를 찾는다..")
    void testFindTotalDistance() {
        //given
        final Sections sections1 = new Sections(List.of(section1, section2, section3));
        final Line line1 = new Line("lineName", "lineColor", sections1);
        final Sections sections2 = new Sections(List.of(section4, section5, section6));
        final Line line2 = new Line("lineName", "lineColor", sections2);
        final Lines lines = new Lines(List.of(line1, line2));
        final Path path = new Path(lines);

        //when
        final long totalDistance = path.findTotalDistanceAsLong(firstStation, fourthStation);

        //then
        assertThat(totalDistance).isEqualTo(3L);
    }
}
