package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static subway.domain.StationFixture.*;

@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void 하나의_섹션에_역이_존재하면_true() {
        Line line = new Line(1L, "2", "green");

        Section section = new Section(JAMSIL, SEONLEUNG, new Distance(10), line);

        Assertions.assertThat(section.contains(JAMSIL)).isTrue();
    }

    @Test
    void 섹션에_역이_존재하지_않으면_false() {
        Line line = new Line(1L, "2", "green");

        Section section = new Section(JAMSIL, SEONLEUNG, new Distance(10), line);

        Assertions.assertThat(section.contains(GANGNAM)).isFalse();
    }
}
