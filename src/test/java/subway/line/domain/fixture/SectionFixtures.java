package subway.line.domain.fixture;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import subway.line.domain.Section;

@SuppressWarnings("NonAsciiCharacters")
public class SectionFixtures {

    public static void 포함된_구간들을_검증한다(final List<Section> sections, final String... sectionStrings) {
        assertThat(sections)
                .extracting(it -> it.up().name() + "-[" + it.distance() + "km]-" + it.down().name())
                .containsExactly(sectionStrings);
    }
}
