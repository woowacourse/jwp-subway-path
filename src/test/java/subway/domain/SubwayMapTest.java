package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.graph.JGraphTSubwayGraph;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SubwayMapTest {


    private final Station STATION1 = new Station("잠실새내");
    private final Station STATION2 = new Station("잠실");
    private final Station STATION3 = new Station("잠실나루");
    private final Station STATION4 = new Station("몽촌토성");
    private final Station STATION5 = new Station("석촌");
    private final Distance DISTANCE1 = new Distance(10);
    private final Distance DISTANCE2 = new Distance(15);
    private final Section SECTION1 = new Section(STATION1, STATION2, DISTANCE1);
    private final Section SECTION2 = new Section(STATION2, STATION3, DISTANCE2);
    private final Section SECTION3 = new Section(STATION4, STATION3, DISTANCE1);
    private final Section SECTION4 = new Section(STATION3, STATION5, DISTANCE2);
    private final List<Section> SECTION_LIST1 = List.of(SECTION1, SECTION2);
    private final List<Section> SECTION_LIST2 = List.of(SECTION3, SECTION4);
    private final Sections SECTIONS1 = new Sections(SECTION_LIST1);
    private final Sections SECTIONS2 = new Sections(SECTION_LIST2);
    private final Line LINE1 = new Line(1L, new LineName("2호선"), new LineColor("초록"), SECTIONS1);
    private final Line LINE2 = new Line(2L, new LineName("8호선"), new LineColor("파랑"), SECTIONS2);
    private final List<Line> LINES = List.of(LINE1, LINE2);

    @DisplayName("출발, 도착을 입력하면 최단 거리 경로를 반환한다.")
    @Test
    void 출발_도착을_입력하면_최단_거리_경로를_반환한다() {
        //given
        SubwayMap subwayMap = new SubwayMap(new Lines(LINES), new JGraphTSubwayGraph());
        //when
        List<Section> sections = subwayMap.calculateShortestPaths(STATION1, STATION5);
        //then
        assertThat(sections).containsExactly(SECTION1, SECTION2, SECTION4);
    }

    @DisplayName("경로의 구간을 입력하면 거리를 반환한다.")
    @Test
    void 구간을_입력하면_거리를_반환한다() {
        //given
        SubwayMap subwayMap = new SubwayMap(new Lines(LINES), new JGraphTSubwayGraph());
        //when
        List<Section> sections = subwayMap.calculateShortestPaths(STATION1, STATION5);
        Integer distance = subwayMap.calculateTotalDistance(sections);
        //then
        assertThat(distance).isEqualTo(40);
    }
}
