package subway.path.application;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static subway.line.domain.fixture.StationFixture.건대입구;
import static subway.line.domain.fixture.StationFixture.선릉;
import static subway.line.domain.fixture.StationFixture.역1;
import static subway.line.domain.fixture.StationFixture.역2;
import static subway.line.domain.fixture.StationFixture.역3;
import static subway.line.domain.fixture.StationFixture.역4;
import static subway.line.domain.fixture.StationFixture.역5;
import static subway.line.domain.fixture.StationFixture.역6;
import static subway.line.domain.fixture.StationFixture.역7;
import static subway.line.domain.fixture.StationFixture.역8;
import static subway.line.domain.fixture.StationFixture.잠실;
import static subway.line.exception.station.StationExceptionType.NOT_FOUND_STATION;
import static subway.path.exception.PathExceptionType.NO_PATH;
import static subway.path.exception.PathExceptionType.START_AND_END_STATIONS_IS_SAME;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.common.exception.BaseExceptionType;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.Section;
import subway.line.domain.StationRepository;
import subway.line.exception.station.StationException;
import subway.path.application.dto.ShortestRouteResponse;
import subway.path.domain.ShortestRouteService;
import subway.path.domain.payment.PaymentPolicy;
import subway.path.exception.PathException;
import subway.path.infrastructure.shortestpath.GraphCache;
import subway.path.infrastructure.shortestpath.JgraphtShortestRoute;
import subway.path.infrastructure.shortestpath.LineDispatcher;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("PathService 은(는)")
class PathServiceTest {

    private final LineRepository lineRepository = mock(LineRepository.class);
    private final StationRepository stationRepository = mock(StationRepository.class);
    private final PaymentPolicy paymentPolicy = mock(PaymentPolicy.class);
    private final ShortestRouteService shortestRouteService =
            new JgraphtShortestRoute(new LineDispatcher(), new GraphCache());
    private final PathService pathService =
            new PathService(stationRepository, lineRepository, shortestRouteService, paymentPolicy);

    @Nested
    class 최단_경로_조회_시 {
        /*
         *    [역1] - [역2] - [역3] - 역4
         *      |            /   \
         *    [역7]        역6    [역5]
         *      |      /
         *      |   /
         *      역8
         *
         *     잠실 - 선릉
         */
        private final List<Line> lines = List.of(
                new Line("1호선",
                        new Section(역1, 역2, 10),
                        new Section(역2, 역3, 5),
                        new Section(역3, 역4, 7)
                ),
                new Line("2호선",
                        new Section(역6, 역3, 7),
                        new Section(역3, 역5, 1)
                ),
                new Line("3호선",
                        new Section(역1, 역7, 10),
                        new Section(역7, 역8, 5),
                        new Section(역8, 역6, 51)
                ),
                new Line("4호선",
                        new Section(잠실, 선릉, 10)
                )
        );

        @BeforeEach
        void setUp() {
            given(lineRepository.findAll()).willReturn(lines);
        }

        @Test
        void 존재하지_않는_역으로_조회한_경우_예외() {
            // given
            given(stationRepository.findByName(건대입구.name())).willReturn(empty());
            given(stationRepository.findByName(역5.name())).willReturn(Optional.of(역5));

            // when
            final BaseExceptionType exceptionType = assertThrows(StationException.class, () ->
                    pathService.findShortestRoute(역5.name(), 건대입구.name())
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(NOT_FOUND_STATION);

        }

        @Test
        void 최단_경로를_조회한다() {
            // given
            given(stationRepository.findByName(역5.name())).willReturn(Optional.of(역5));
            given(stationRepository.findByName(역7.name())).willReturn(Optional.of(역7));
            given(paymentPolicy.calculateFee(any())).willReturn(2000);

            // when
            final ShortestRouteResponse shortestRoute = pathService.findShortestRoute(역5.name(), 역7.name());

            // then
            assertThat(shortestRoute.getTotalDistance()).isEqualTo(26);
            assertThat(shortestRoute.getSectionInfos())
                    .extracting(it ->
                            it.getLine() + ": " +
                                    it.getFromStation() + "-[" + it.getDistance() + "km]-" + it.getToStation()
                    ).containsExactly(
                            "2호선: 역5-[1km]-역3",
                            "1호선: 역3-[5km]-역2",
                            "1호선: 역2-[10km]-역1",
                            "3호선: 역1-[10km]-역7"
                    );
            assertThat(shortestRoute.getTransferCount()).isEqualTo(2);
            assertThat(shortestRoute.getTotalFee()).isEqualTo(2000);
            assertThat(shortestRoute.getTransferStations())
                    .containsExactly("역3", "역1");
        }

        @Test
        void 경로가_없는_경우_예외이다() {
            // given
            given(stationRepository.findByName(역5.name())).willReturn(Optional.of(역5));
            given(stationRepository.findByName(잠실.name())).willReturn(Optional.of(잠실));

            // when
            final BaseExceptionType exceptionType = assertThrows(PathException.class, () ->
                    pathService.findShortestRoute(역5.name(), 잠실.name())
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(NO_PATH);
        }

        @Test
        void 출발역과_종착역이_동일한_경우_예외() {
            // given
            given(stationRepository.findByName(역5.name())).willReturn(Optional.of(역5));

            // when
            final BaseExceptionType exceptionType = assertThrows(PathException.class, () ->
                    pathService.findShortestRoute(역5.name(), 역5.name())
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(START_AND_END_STATIONS_IS_SAME);
        }
    }
}
