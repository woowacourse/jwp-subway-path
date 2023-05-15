package subway.path.infrastructure.shortestpath;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.line.domain.fixture.StationFixture.역1;
import static subway.line.domain.fixture.StationFixture.역2;
import static subway.line.domain.fixture.StationFixture.역3;
import static subway.line.domain.fixture.StationFixture.역4;
import static subway.line.domain.fixture.StationFixture.역5;
import static subway.line.domain.fixture.StationFixture.역6;
import static subway.line.domain.fixture.StationFixture.역7;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.path.domain.Path;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineDispatcher 은(는)")
class LineDispatcherTest {

    private final LineDispatcher lineDispatcher = new LineDispatcher();

    private final Path path = new Path(
            new Line("1호선", 0,
                    new Section(역1, 역2, 1),
                    new Section(역2, 역3, 2),
                    new Section(역3, 역4, 3)
            ),
            new Line("2호선", 0,
                    new Section(역4, 역2, 4),
                    new Section(역2, 역5, 5),
                    new Section(역5, 역7, 6)
            ),
            new Line("3호선", 0,
                    new Section(역7, 역6, 7),
                    new Section(역6, 역1, 8),
                    new Section(역1, 역3, 9)
            )
    );

    private final List<Section> sections = List.of(
            new Section(역4, 역2, 4),  // 2호선
            new Section(역2, 역3, 2),  // 1호선
            new Section(역1, 역3, 9),  // 3호선
            new Section(역1, 역2, 1),  // 1호선
            new Section(역2, 역3, 2),  // 1호선
            new Section(역3, 역4, 3)   // 1호선
    );

    @Test
    void 섹션이_속한_노선을_찾아준다() {
        // when
        final Path dispatch = lineDispatcher.dispatch(path, sections);

        // then
        final List<Line> lines = dispatch.lines();
        final Line 이호선 = lines.get(0);
        final Line 일호선1 = lines.get(1);
        final Line 삼호선 = lines.get(2);
        final Line 일호선2 = lines.get(3);

        assertAll(
                () -> assertThat(이호선.name()).isEqualTo("2호선"),
                () -> assertThat(이호선.sections()).containsExactly(new Section(역4, 역2, 4)),
                () -> assertThat(일호선1.name()).isEqualTo("1호선"),
                () -> assertThat(일호선1.sections()).containsExactly(new Section(역2, 역3, 2)),
                () -> assertThat(삼호선.name()).isEqualTo("3호선"),
                () -> assertThat(삼호선.sections()).containsExactly(new Section(역1, 역3, 9)),
                () -> assertThat(일호선2.name()).isEqualTo("1호선"),
                () -> assertThat(일호선2.sections())
                        .containsExactly(
                                new Section(역1, 역2, 1),
                                new Section(역2, 역3, 2),
                                new Section(역3, 역4, 3)
                        )
        );
    }
}
