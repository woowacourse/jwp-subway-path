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
    void 경로_상의_모든_구간을_반환한다() {
        // given
        final PathFindResult pathFindResult = new PathFindResult(new Distance(5), List.of(
                new SectionEdge(new Section("A", "B", 5), 500),
                new SectionEdge(new Section("B", "C", 10), 500)
        ));

        // when
        final List<Section> sections = pathFindResult.toSections();

        // then
        assertThat(sections).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 10)
        ));
    }
}
