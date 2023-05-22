package subway.application.service.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.application.port.in.route.dto.command.FindRouteCommand;
import subway.application.port.in.route.dto.response.RouteQueryResponse;
import subway.application.port.out.line.LoadLinePort;
import subway.application.port.out.route.RouteFinderPort;
import subway.application.port.out.station.LoadStationPort;
import subway.common.exception.NoSuchStationException;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.fare.Fare;
import subway.domain.fare.FarePolicy;
import subway.domain.route.Route;
import subway.domain.route.RouteSection;
import subway.fixture.StationFixture.강남역;
import subway.fixture.StationFixture.삼성역;

@DisplayName("경로 조회시 ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RouteQueryServiceTest {

    private RouteQueryService routeQueryService;
    private LoadLinePort loadLinePort;
    private LoadStationPort loadStationPort;
    private RouteFinderPort routeFinderPort;
    private FarePolicy farePolicy;


    @BeforeEach
    void setUp() {
        loadLinePort = mock(LoadLinePort.class);
        loadStationPort = mock(LoadStationPort.class);
        routeFinderPort = mock(RouteFinderPort.class);
        farePolicy = mock(FarePolicy.class);
        routeQueryService = new RouteQueryService(loadLinePort, loadStationPort, routeFinderPort, farePolicy);
    }

    @Nested
    class 경로_조회시_ {

        private long sourceId = 1L;
        private long targetId = 2L;
        private Station source = new Station(sourceId, "역삼역");
        private Station target = new Station(sourceId, "잠실역");
        private FindRouteCommand command = new FindRouteCommand(sourceId, targetId);

        @BeforeEach
        void setUp() {
            given(loadStationPort.findById(sourceId))
                    .willReturn(Optional.of(source));
            given(loadStationPort.findById(targetId))
                    .willReturn(Optional.of(target));
        }

        @Test
        void 출발역에_해당하는_역이_존재하지_않으면_예외() {
            // given then
            given(loadStationPort.findById(sourceId))
                    .willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> routeQueryService.findRoute(new FindRouteCommand(sourceId, targetId)))
                    .isInstanceOf(NoSuchStationException.class);
        }

        @Test
        void 도착역에_해당하는_역이_존재하지_않으면_예외() {
            // given
            given(loadStationPort.findById(targetId))
                    .willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> routeQueryService.findRoute(new FindRouteCommand(sourceId, targetId)))
                    .isInstanceOf(NoSuchStationException.class);
        }

        @Test
        void 성공() {
            // given
            Line line = new Line("이호선", "GREEN", 0);
            List<Line> lines = List.of(line);
            Route route = new Route(List.of(
                    new RouteSection(line, new Section(source, 강남역.STATION, 2)),
                    new RouteSection(line, new Section(강남역.STATION, 삼성역.STATION, 2)),
                    new RouteSection(line, new Section(삼성역.STATION, target, 6))
            ));
            given(loadLinePort.findAll())
                    .willReturn(lines);
            given(routeFinderPort.findRoute(source, target, lines))
                    .willReturn(route);
            given(farePolicy.calculate(route))
                    .willReturn(new Fare(1250));

            // when
            RouteQueryResponse response = routeQueryService.findRoute(command);

            // then
            assertAll(
                    () -> assertThat(response.getDistance()).isEqualTo(10),
                    () -> assertThat(response.getRoute())
                            .extracting("name")
                            .containsExactly(
                                    source.getName(),
                                    강남역.STATION.getName(),
                                    삼성역.STATION.getName(),
                                    target.getName()),
                    () -> assertThat(response.getFare())
                            .isEqualTo(1250)
            );
        }
    }
}
