package subway.domain;

import org.junit.jupiter.api.Test;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.StationFixture.GANGNAM;
import static subway.domain.StationFixture.JAMSIL;
import static subway.domain.StationFixture.SEONLEUNG;

@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void 하나의_섹션에_역이_존재하면_true() {
        Section section = new Section(JAMSIL, SEONLEUNG, new Distance(10));

        assertThat(section.contains(JAMSIL)).isTrue();
    }

    @Test
    void 섹션에_역이_존재하지_않으면_false() {
        Section section = new Section(JAMSIL, SEONLEUNG, new Distance(10));

        assertThat(section.contains(GANGNAM)).isFalse();
    }

    @Test
    void 섹션의_거리_정보를_이용해_새로운_노선의_길이를_구한다() {
        Distance originalDistance = new Distance(10);
        Section originalSection = new Section(JAMSIL, SEONLEUNG, originalDistance);

        Distance reducedDistance = originalSection.calculateNewSectionDistance(new Distance(4));

        assertThat(reducedDistance).isEqualTo(new Distance(6));
    }
}
