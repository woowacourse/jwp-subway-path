package subway.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.StationFixture.GANGNAM;
import static subway.fixture.StationFixture.JAMSIL;
import static subway.fixture.StationFixture.SEONLEUNG;

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

    @ParameterizedTest
    @ValueSource(ints = {15, 10})
    void 기존_섹션의_거리가_새로운_경로의_거리보다_작으면_예외(int distance) {
        Section section = new Section(JAMSIL, SEONLEUNG, new Distance(10));

        assertThat(section.isSmaller(new Distance(distance))).isTrue();
    }

    @Test
    void 섹션의_upStation_인지_확인한다() {
        Section section = new Section(JAMSIL, SEONLEUNG, new Distance(10));
        Assertions.assertAll(
                () -> assertThat(section.isUpStation(JAMSIL)).isTrue(),
                () -> assertThat(section.isUpStation(SEONLEUNG)).isFalse()
        );
    }

    @Test
    void 섹션의_downStation_인지_확인한다() {
        Section section = new Section(JAMSIL, SEONLEUNG, new Distance(10));
        Assertions.assertAll(
                () -> assertThat(section.isDownStation(JAMSIL)).isFalse(),
                () -> assertThat(section.isDownStation(SEONLEUNG)).isTrue()
        );
    }

    @Test
    void 두_개의_섹션_거리를_합한다() {
        Section firstSection = new Section(JAMSIL, SEONLEUNG, new Distance(10));
        Section secondSection = new Section(SEONLEUNG, GANGNAM, new Distance(7));

        Distance distance = firstSection.calculateCombineDistance(secondSection);
        assertThat(distance).isEqualTo(new Distance(17));
    }
}
