package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.core.Distance;
import subway.domain.core.Section;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PathFindResultTest {

    @Test
    void 경로_상의_모든_구간을_라인_기준으로_반환한다() {
        // given
        final PathFindResult pathFindResult = new PathFindResult(new Distance(5), List.of(
                new SectionEdge(new Section("A", "B", 5), 500, 1),
                new SectionEdge(new Section("B", "C", 10), 500, 1),
                new SectionEdge(new Section("C", "T", 10), 500, 2),
                new SectionEdge(new Section("T", "D", 10), 500, 1)
        ));

        // when
        final List<List<Section>> result = pathFindResult.toSections();

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(List.of(
                List.of(
                        new Section("A", "B", 5),
                        new Section("B", "C", 10)
                ),
                List.of(
                        new Section("C", "T", 10)
                ),
                List.of(
                        new Section("T", "D", 10)
                )
        ));
    }
}
