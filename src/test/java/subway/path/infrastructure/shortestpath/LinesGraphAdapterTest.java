package subway.path.infrastructure.shortestpath;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.domain.fixture.StationFixture.역1;
import static subway.line.domain.fixture.StationFixture.역2;
import static subway.line.domain.fixture.StationFixture.역3;
import static subway.line.domain.fixture.StationFixture.역4;
import static subway.line.domain.fixture.StationFixture.역5;
import static subway.line.domain.fixture.StationFixture.역6;
import static subway.line.domain.fixture.StationFixture.역7;
import static subway.line.domain.fixture.StationFixture.역8;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.path.domain.Path;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LinesGraphAdapter 은(는)")
class LinesGraphAdapterTest {

    /*
     *     역1 - 역2 - 역3 - 역4
     *      |        /  \
     *     역7     역6    역5
     *      |   /
     *     역8
     */
    private final Path path = new Path(
            new Line("1호선", 0,
                    new Section(역1, 역2, 10),
                    new Section(역2, 역3, 5),
                    new Section(역3, 역4, 7)
            ),
            new Line("2호선", 0,
                    new Section(역6, 역3, 7),
                    new Section(역3, 역5, 1)
            ),
            new Line("3호선", 0,
                    new Section(역1, 역7, 10),
                    new Section(역7, 역8, 5)
            )
    );

    @Test
    void Line_들의_정보를_통해_생성된다() {
        // when
        final LinesGraphAdapter graph = LinesGraphAdapter.adapt(path);

        // then
        assertThat(graph.vertexSet())
                .containsExactlyInAnyOrder(역1, 역2, 역3, 역4, 역5, 역6, 역7, 역8);
        assertThat(graph.edgeSet())
                .extracting(SectionAdapter::toSection)
                .containsExactlyInAnyOrder(
                        new Section(역1, 역2, 10),
                        new Section(역2, 역3, 5),
                        new Section(역3, 역4, 7),
                        new Section(역6, 역3, 7),
                        new Section(역3, 역5, 1),
                        new Section(역1, 역7, 10),
                        new Section(역7, 역8, 5)
                );
    }
}
