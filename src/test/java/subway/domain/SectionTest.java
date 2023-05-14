package subway.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import subway.TestSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SectionTest {

    @Test
    void 주어진_id의_역이_있는지_판별한다() {
        Station cheonho = new Station(1L, "천호");
        Station jamsil = new Station(2L, "잠실");
        Section cheonhoJamsil10 = new Section(cheonho, jamsil, new Line("8호선", "pink"), 10);

        assertThat(cheonhoJamsil10.getStationWithGivenId(1L)).isPresent();
        assertThat(cheonhoJamsil10.getStationWithGivenId(3L)).isEmpty();
    }

    @Test
    void 주어진_역이_구간의_상행_역인지_판별한다() {
        Section cheonhoJamsil10 = TestSource.cheonhoJamsil10;

        assertTrue(cheonhoJamsil10.isUpStationGiven(TestSource.cheonho));
        assertFalse(cheonhoJamsil10.isUpStationGiven(TestSource.jamsil));
    }

    @Test
    void 주어진_역이_구간의_하행_역인지_판별한다() {
        Section cheonhoJamsil10 = TestSource.cheonhoJamsil10;

        assertTrue(cheonhoJamsil10.isDownStationGiven(TestSource.jamsil));
        assertFalse(cheonhoJamsil10.isDownStationGiven(TestSource.cheonho));
    }

    @Test
    void 주어진_길이만큼_감소된_구간_길이를_반환한다() {
        Section cheonhoJamsil10 = TestSource.cheonhoJamsil10;
        int reducedDistance = 4;

        assertThat(cheonhoJamsil10.getReducedDistanceBy(reducedDistance)).isEqualTo(6);
    }

    @Test
    void 감소될_길이가_기존의_길이보다_크면_예외를_반환한다() {
        Section cheonhoJamsil10 = TestSource.cheonhoJamsil10;
        int reducedDistance = 11;

        assertThatThrownBy(() -> cheonhoJamsil10.getReducedDistanceBy(reducedDistance))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("변경된 구간의 길이가 1 미만입니다");
    }
}
