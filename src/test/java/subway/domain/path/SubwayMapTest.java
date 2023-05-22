package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;

@SuppressWarnings("NonAsciiCharacters")
class SubwayMapTest {

    @Test
    void 여러_노선으로_지하철_전체_지도_생성_테스트() {
        //given
        final Sections sectionsA = new Sections(List.of(
                new Section(new Station("강남"), new Station("역삼"), new StationDistance(3)),
                new Section(new Station("역삼"), new Station("선릉"), new StationDistance(2))
        ));
        final Line lineA = new Line(sectionsA, new LineName("1호선"), new LineColor("파랑색"));

        final Sections sectionsB = new Sections(List.of(
                new Section(new Station("강남구청"), new Station("선정릉"), new StationDistance(5)),
                new Section(new Station("선정릉"), new Station("선릉"), new StationDistance(8))
        ));
        final Line lineB = new Line(sectionsB, new LineName("2호선"), new LineColor("청록색"));

        //when
        final SubwayMap subwayMap = SubwayMap.from(List.of(lineA, lineB));

        final WeightedMultigraph<Station, DefaultWeightedEdge> stationsGraph = subwayMap.getStationsGraph();
        assertThat(stationsGraph.containsEdge(new Station("강남"), new Station("역삼"))).isTrue();
        assertThat(stationsGraph.containsEdge(new Station("역삼"), new Station("선릉"))).isTrue();
        assertThat(stationsGraph.vertexSet()).hasSize(5);
        assertThat(stationsGraph.edgeSet()).hasSize(4);
    }

    @Test
    void 출발점과_도착점을_기준으로_최단_경로_조회_테스트() {
        // 1호선: 강남    --(3)-- 역삼  --(2)-- 선릉
        // 2호선: 강남구청 --(5)-- 선정릉 --(8)-- 선릉
        //
        // 3호선: 사당    --(2)-- 이수  --(6)-- 동작
        final SubwayMap subwayMap = subwayMapFixture();
        final Station fromStation = new Station("역삼");
        final Station toStation = new Station("강남구청");

        final SubwayPath shortestPath = subwayMap.findShortestPath(fromStation, toStation);

        assertThat(shortestPath.getStations()).extractingResultOf("getStationName")
                .containsExactly("역삼", "선릉", "선정릉", "강남구청");
        assertThat(shortestPath.getDistance()).isEqualTo(2 + 8 + 5);
    }

    @Test
    void 최단_경로_조회시_역이_존재하지_않으면_예외_발생() {
        // 1호선: 강남    --(3)-- 역삼  --(2)-- 선릉
        // 2호선: 강남구청 --(5)-- 선정릉 --(8)-- 선릉
        //
        // 3호선: 사당    --(2)-- 이수  --(6)-- 동작
        final SubwayMap subwayMap = subwayMapFixture();
        final Station fromStation = new Station("역삼");
        final Station toStation = new Station("존재하지않는역");

        assertThatThrownBy(() -> subwayMap.findShortestPath(fromStation, toStation))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 최단_경로_조회시_역이_연결되어_있지_않으면_예외_발생() {
        // 1호선: 강남    --(3)-- 역삼  --(2)-- 선릉
        // 2호선: 강남구청 --(5)-- 선정릉 --(8)-- 선릉
        //
        // 3호선: 사당    --(2)-- 이수  --(6)-- 동작
        final SubwayMap subwayMap = subwayMapFixture();
        final Station fromStation = new Station("역삼");
        final Station toStation = new Station("이수");

        assertThatThrownBy(() -> subwayMap.findShortestPath(fromStation, toStation))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("연결 되지 않는 역 정보입니다");
    }


    private SubwayMap subwayMapFixture() {
        final Sections sectionsA = new Sections(List.of(
                new Section(new Station("강남"), new Station("역삼"), new StationDistance(3)),
                new Section(new Station("역삼"), new Station("선릉"), new StationDistance(2))
        ));
        final Line lineA = new Line(sectionsA, new LineName("1호선"), new LineColor("파랑색"));

        final Sections sectionsB = new Sections(List.of(
                new Section(new Station("강남구청"), new Station("선정릉"), new StationDistance(5)),
                new Section(new Station("선정릉"), new Station("선릉"), new StationDistance(8))
        ));
        final Line lineB = new Line(sectionsB, new LineName("2호선"), new LineColor("청록색"));

        final Sections sectionsC = new Sections(List.of(
                new Section(new Station("사당"), new Station("이수"), new StationDistance(2)),
                new Section(new Station("이수"), new Station("동작"), new StationDistance(6))
        ));
        final Line lineC = new Line(sectionsC, new LineName("3호선"), new LineColor("하늘색"));

        return SubwayMap.from(List.of(lineA, lineB, lineC));
    }
}
