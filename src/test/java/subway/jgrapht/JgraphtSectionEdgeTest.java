package subway.jgrapht;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.core.Section;
import subway.domain.path.SectionEdge;
import subway.jgraph.JgraphtSectionEdge;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JgraphtSectionEdgeTest {

    @Test
    void Section을_반환한다() {
        // given
        final SectionEdge sectionEdge = new JgraphtSectionEdge(new Section("A", "B", 5), 500, 1);

        // when
        final Section result = sectionEdge.toSection();

        // then
        assertThat(result).isEqualTo(new Section("A", "B", 5));
    }
}
