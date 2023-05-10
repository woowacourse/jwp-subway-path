package subway.domain.fixture;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import subway.domain.Section;
import subway.domain.Station;

@SuppressWarnings("NonAsciiCharacters")
public class SectionFixtures {

    public static Section createSection(final String upStationName, final String downStationName, final int distance) {
        return new Section(new Station(upStationName), new Station(downStationName), distance);
    }

    public static void 포함된_구간들을_검증한다(final List<Section> sections, final String... sectionStrings) {
        assertThat(sections)
                .extracting(it -> it.up().name() + "-[" + it.distance() + "km]-" + it.down().name())
                .containsExactly(sectionStrings);
    }
}
