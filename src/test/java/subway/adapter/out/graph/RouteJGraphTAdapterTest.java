package subway.adapter.out.graph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.exception.SubwayIllegalArgumentException;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.domain.route.Route;
import subway.fixture.SectionFixture.이호선_삼성_잠실_2;
import subway.fixture.SectionFixture.이호선_역삼_삼성_3;
import subway.fixture.StationFixture.강남역;
import subway.fixture.StationFixture.고터역;
import subway.fixture.StationFixture.교대역;
import subway.fixture.StationFixture.논현역;
import subway.fixture.StationFixture.반포역;
import subway.fixture.StationFixture.방배역;
import subway.fixture.StationFixture.서초역;
import subway.fixture.StationFixture.신논현역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@DisplayName("최단 경로 조회시 ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RouteJGraphTAdapterTest {

    private RouteFinderJGraphTAdapter routeJGraphTAdapter;

    @BeforeEach
    void setUp() {
        routeJGraphTAdapter = new RouteFinderJGraphTAdapter();
    }

    @Test
    void 출발역과_도착역이_같을시_예외() {
        // given
        Station source = 역삼역.STATION;
        Station target = 역삼역.STATION;
        Line line = new Line(1L, "2호선", "GREEN", 0, List.of(이호선_역삼_삼성_3.SECTION));
        List<Line> lines = List.of(line);

        // when then
        assertThatThrownBy(() -> routeJGraphTAdapter.findRoute(source, target, lines))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 같습니다.");
    }

    @Test
    void 출발역이_경로에_포함되지_않은_역일시_예외() {
        // given
        Station source = 역삼역.STATION;
        Station target = 잠실역.STATION;
        Line line = new Line(1L, "2호선", "GREEN", 0, List.of(이호선_삼성_잠실_2.SECTION));
        List<Line> lines = List.of(line);

        // when then
        assertThatThrownBy(() -> routeJGraphTAdapter.findRoute(source, target, lines))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("경로상에 역이 존재하지 않습니다.");
    }

    @Test
    void 도착역이_경로에_포함되지_않은_역일시_예외() {
        // given
        Station source = 역삼역.STATION;
        Station target = 잠실역.STATION;
        Line line = new Line(1L, "2호선", "GREEN", 0, List.of(이호선_역삼_삼성_3.SECTION));
        List<Line> lines = List.of(line);

        // when then
        assertThatThrownBy(() -> routeJGraphTAdapter.findRoute(source, target, lines))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("경로상에 역이 존재하지 않습니다.");
    }

    @Test
    void 동일_경로가_존재할시_더_짧은_경로를_선택한다() {
        // given
        Station source = 역삼역.STATION;
        Station target = 잠실역.STATION;
        Line line1 = new Line(1L, "2호선", "GREEN", 0, List.of(new Section(source, target, 3)));
        Line line2 = new Line(2L, "3호선", "ORANGE", 0, List.of(new Section(source, target, 4)));
        List<Line> lines = List.of(line1, line2);

        // when
        Route route = routeJGraphTAdapter.findRoute(source, target, lines);

        // then
        assertThat(route.findTotalDistance()).isEqualTo(3);
    }

    @Test
    void 동일_경로에_거리까지_같으면_노선_추가비용이_더_작은_경로를_선택한다() {
        // given
        Station source = 역삼역.STATION;
        Station target = 잠실역.STATION;
        Line line1 = new Line(1L, "1호선", "BLUE", 2000, List.of(new Section(source, target, 3)));
        Line line2 = new Line(2L, "2호선", "GREEN", 1000, List.of(new Section(source, target, 3)));
        Line line3 = new Line(3L, "3호선", "ORANGE", 3000, List.of(new Section(source, target, 4)));
        List<Line> lines = List.of(line1, line2, line3);

        // when
        Route route = routeJGraphTAdapter.findRoute(source, target, lines);

        // then
        assertThat(route.findLines()).containsExactly(line2);
    }

    @Test
    void 상행과_하행_방향_상관없이_경로를_조회할_수_있다() {
        // given
        Station source = 역삼역.STATION;
        Station target = 잠실역.STATION;
        List<Line> upLine = List.of(new Line(1L, "2호선", "GREEN", 0, List.of(new Section(source, target, 3))));
        List<Line> downLine = List.of(new Line(1L, "2호선", "GREEN", 0, List.of(new Section(source, target, 3))));

        // when
        Route upRoute = routeJGraphTAdapter.findRoute(source, target, upLine);
        Route downRoute = routeJGraphTAdapter.findRoute(source, target, downLine);

        // then
        assertThat(upRoute)
                .usingRecursiveComparison()
                .isEqualTo(downRoute);
    }

    @Test
    void 최단거리_경로를_조회한다() {
        // given
        Line 이호선 = new Line(1L, "2호선", "GREEN", 0, List.of(
                new Section(방배역.STATION, 서초역.STATION, 1),
                new Section(서초역.STATION, 교대역.STATION, 1),
                new Section(교대역.STATION, 강남역.STATION, 2)
        ));

        Line 삼호선 = new Line(2L, "3호선", "ORANGE", 0, List.of(
                new Section(교대역.STATION, 고터역.STATION, 2)
        ));
        Line 신분당선 = new Line(3L, "신분당선", "RED", 0, List.of(
                new Section(강남역.STATION, 신논현역.STATION, 3),
                new Section(신논현역.STATION, 논현역.STATION, 4)
        ));
        Line 칠호선 = new Line(4L, "7호선", "DARK_GREEN", 0, List.of(
                new Section(고터역.STATION, 반포역.STATION, 2),
                new Section(반포역.STATION, 논현역.STATION, 2)
        ));

        // when
        Route route = routeJGraphTAdapter.findRoute(방배역.STATION, 논현역.STATION, List.of(이호선, 삼호선, 신분당선, 칠호선));

        // then
        assertAll(
                () -> assertThat(route.findStationRoute())
                        .containsExactly(방배역.STATION, 서초역.STATION, 교대역.STATION, 고터역.STATION, 반포역.STATION, 논현역.STATION),
                () -> assertThat(route.findTotalDistance())
                        .isEqualTo(8),
                () -> assertThat(route.findLines())
                        .containsExactly(이호선, 삼호선, 칠호선)
        );
    }

    @Test
    void 도달할_수_없을_경우_예외() {
        // given
        Line 이호선 = new Line(1L, "2호선", "GREEN", 0, List.of(
                new Section(방배역.STATION, 서초역.STATION, 1),
                new Section(서초역.STATION, 교대역.STATION, 1),
                new Section(교대역.STATION, 강남역.STATION, 2)
        ));
        Line 칠호선 = new Line(4L, "7호선", "DARK_GREEN", 0, List.of(
                new Section(고터역.STATION, 반포역.STATION, 2),
                new Section(반포역.STATION, 논현역.STATION, 2)
        ));

        // when
        assertThatThrownBy(() -> routeJGraphTAdapter.findRoute(방배역.STATION, 논현역.STATION, List.of(이호선, 칠호선)))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("도달할 수 없습니다.");
    }
}
