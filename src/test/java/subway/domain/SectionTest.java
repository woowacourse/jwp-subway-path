package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.exception.invalid.DistanceInvalidException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SectionTest {

    private Station jamsil = new Station("잠실역");
    private Station gangnam = new Station("강남역");

    @Test
    void 역_구간의_길이보다_크거나_같은_길이에_대해서_예외를_던진다() {
        final Section section = new Section(jamsil, gangnam, 3L);

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThatThrownBy(() -> section.validateDistance(3L))
                .isInstanceOf(DistanceInvalidException.class);
        softAssertions.assertThatThrownBy(() -> section.validateDistance(4L))
                .isInstanceOf(DistanceInvalidException.class);
        softAssertions.assertAll();
    }

    @Test
    void 역_구간_안에_특정_역이_존재하는지_확인한다() {
        final Section section = new Section(jamsil, gangnam, 3L);

        assertThat(section.contains(new Station("비빔밥"))).isFalse();
    }

    @Test
    void 역_구간_정보가_같다면_동등하다() {
        final Section section1 = new Section(jamsil, gangnam, 3L);
        final Section section2 = new Section(jamsil, gangnam, 3L);

        assertThat(section1).isEqualTo(section2);
    }
}
