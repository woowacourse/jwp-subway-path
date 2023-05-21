package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.core.Section;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PathTest {

    @Test
    void 경로의_총_거리를_반환한다() {
        // given
        final Path path = new Path(List.of(
                new SectionEdge(new Section("A", "B", 2), 300, 1),
                new SectionEdge(new Section("B", "C", 5), 300, 1),
                new SectionEdge(new Section("C", "T", 7), 500, 2),
                new SectionEdge(new Section("T", "D", 4), 300, 1)
        ));

        // when
        final int result = path.calculateTotalDistance();

        // then
        assertThat(result).isEqualTo(18);
    }
}
