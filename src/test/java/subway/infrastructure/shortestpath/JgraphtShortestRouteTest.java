package subway.infrastructure.shortestpath;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.domain.fixture.StationFixture.건대입구;
import static subway.domain.fixture.StationFixture.선릉;
import static subway.domain.fixture.StationFixture.역1;
import static subway.domain.fixture.StationFixture.역2;
import static subway.domain.fixture.StationFixture.역3;
import static subway.domain.fixture.StationFixture.역4;
import static subway.domain.fixture.StationFixture.역5;
import static subway.domain.fixture.StationFixture.역6;
import static subway.domain.fixture.StationFixture.역7;
import static subway.domain.fixture.StationFixture.역8;
import static subway.domain.fixture.StationFixture.잠실;
import static subway.domain.fixture.StationFixture.홍대입구;
import static subway.exception.line.LineExceptionType.NOT_EXIST_STATION_IN_LINES;
import static subway.exception.line.LineExceptionType.NO_PATH;
import static subway.exception.line.LineExceptionType.START_AND_END_STATIONS_IS_SAME;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.service.ShortestRouteService;
import subway.exception.BaseExceptionType;
import subway.exception.line.LineException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("JgraphtShortestRoute 은(는)")
class JgraphtShortestRouteTest {

    private final ShortestRouteService shortestRouteService = new JgraphtShortestRoute();

    private final Lines lines = new Lines(
            new Line("1호선", new Sections(List.of(
                    new Section(역1, 역2, 10),
                    new Section(역2, 역3, 5),
                    new Section(역3, 역4, 7)
            ))),
            new Line("2호선", new Sections(List.of(
                    new Section(역6, 역3, 7),
                    new Section(역3, 역5, 1)
            ))),
            new Line("3호선", new Sections(List.of(
                    new Section(역1, 역7, 10),
                    new Section(역7, 역8, 5),
                    new Section(역8, 역6, 51)
            ))),
            new Line("4호선", new Sections(List.of(
                    new Section(잠실, 선릉, 10)
            )))
    );

    @Test
    void 주어진_노선들_속에서_출발역과_종점역_사이의_최단_경로를_구할_수_있다() {
        // when
        // 정렬되지 않고, 단순히 간선들만 나온다
        final Lines shortestLines = shortestRouteService.shortestRoute(lines, 역5, 역7);

        // then
        assertThat(shortestLines.lines())
                .flatMap(Line::sections)
                .containsExactly(
                        new Section(역3, 역5, 1),

                        new Section(역1, 역2, 10),
                        new Section(역2, 역3, 5),

                        new Section(역1, 역7, 10)
                );
        assertThat(shortestLines.totalDistance()).isEqualTo(26);
    }

    @Test
    void 역은_모두_존재하나_경로가_없는경우_예외() {
        // when & then
        final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                shortestRouteService.shortestRoute(lines, 역5, 잠실)
        ).exceptionType();
        assertThat(exceptionType).isEqualTo(NO_PATH);
    }

    @Test
    void 출발지와_종착지가_동일하다면_예외() {
        // when
        final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                shortestRouteService.shortestRoute(lines, 역1, 역1)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(START_AND_END_STATIONS_IS_SAME);
    }

    @Test
    void 출발역이나_종착역이_해당_노선들에_존재하지_않으면_예외() {
        // when & then
        출발역이나_종착역이_해당_노선들에_존재하지_않는경우_예외_검증(
                () -> shortestRouteService.shortestRoute(lines, 역1, 건대입구),
                () -> shortestRouteService.shortestRoute(lines, 건대입구, 홍대입구)
        );
    }

    private void 출발역이나_종착역이_해당_노선들에_존재하지_않는경우_예외_검증(final Runnable... commands) {
        for (final Runnable runnable : commands) {
            final BaseExceptionType exceptionType = assertThrows(LineException.class,
                    runnable::run
            ).exceptionType();
            assertThat(exceptionType).isEqualTo(NOT_EXIST_STATION_IN_LINES);
        }
    }
}

