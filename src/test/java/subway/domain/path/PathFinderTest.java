package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.core.Line;
import subway.domain.core.Section;
import subway.domain.core.Subway;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PathFinderTest {

    @Test
    void 최단_경로를_탐색한다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line(1L, "1호선", "RED", 0, List.of(
                        new Section("A", "B", 2),
                        new Section("B", "C", 3)
                )),
                new Line(2L, "2호선", "RED", 300, List.of(
                        new Section("Z", "B", 4),
                        new Section("B", "Y", 5)
                ))
        ));
        final PathFinder pathFinder = new PathFinder(subway);

        // when
        final Path path = pathFinder.find("A", "Y");

        // then
        assertAll(
                () -> assertThat(path.calculateTotalDistance()).isEqualTo(7),
                () -> assertThat(path.getSectionEdges())
                        .extracting(SectionEdge::toSection)
                        .usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(List.of(
                                new Section("A", "B", 2),
                                new Section("B", "Y", 5)
                        ))
        );
    }
}
