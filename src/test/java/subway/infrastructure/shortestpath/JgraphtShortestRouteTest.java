package subway.infrastructure.shortestpath;

import static org.assertj.core.api.Assertions.assertThat;
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

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.service.ShortestRouteService;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("JgraphtShortestRoute 은(는)")
class JgraphtShortestRouteTest {

    private final ShortestRouteService shortestRouteService = new JgraphtShortestRoute();

    /*
     *     역1 - 역2 - 역3 - 역4
     *      |        /  \
     *     역7     역6    역5
     *      |   /
     *     역8
     *
     *     잠실 - 선릉
     */
    private final List<Line> lines = List.of(
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
        final List<Line> shortestRoute = shortestRouteService.shortestRoute(lines, 역5, 역7);

        // then
        final Line line2 = shortestRoute.get(0);
        final Line line1 = shortestRoute.get(1);
        final Line line3 = shortestRoute.get(2);
        assertThat(line2.sections())
                .containsExactly(
                        new Section(역3, 역5, 1)
                );
        assertThat(line1.sections())
                .containsExactly(
                        new Section(역1, 역2, 10),
                        new Section(역2, 역3, 5)
                );
        assertThat(line3.sections())
                .containsExactly(
                        new Section(역1, 역7, 10)
                );
    }

    @Test
    void 역은_모두_존재하나_경로가_없는경우_빈_list_반환() {
        // given
        final List<Line> shortestRoute = shortestRouteService.shortestRoute(lines, 역5, 잠실);

        // when & then
        assertThat(shortestRoute).isEmpty();
    }
}

