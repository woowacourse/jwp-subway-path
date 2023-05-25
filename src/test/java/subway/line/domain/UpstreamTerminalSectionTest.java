package subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.exception.DuplicateStationInLineException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.line.domain.SectionFixture.*;
import static subway.utils.StationFixture.JAMSIL_NARU_STATION;

class UpstreamTerminalSectionTest {

    @Test
    @DisplayName("상행역으로 DummyTerminalStation을 둘 수 없다")
    void UpTerminalSectionFail() {
        assertThatThrownBy(() -> new UpstreamTerminalSection(DummyTerminalStation.getInstance()))
                .isInstanceOf(DuplicateStationInLineException.class);
    }

    @Test
    @DisplayName("중간에 역을 삽입하면 (기존 상행역)--(삽입한 역), (삽입한 역)--(기존 하행역)이 순서대로 반환된다")
    void insertInTheMiddle() {
        final UpstreamTerminalSection givenSection = new UpstreamTerminalSection(TERMINAL_TO_JAMSIL);

        final List<AbstractSection> actualSections = givenSection.insertInTheMiddle(JAMSIL_NARU_STATION, DISTANCE);

        assertThat(actualSections).containsSequence(TERMINAL_TO_JAMSIL_NARU, JAMSIL_NARU_TO_JAMSIL);
    }

    @Test
    @DisplayName("중간에 DummyTerminalStation을 삽입할 수 없다")
    void insertInTheMiddleFail() {
        final UpstreamTerminalSection givenSection = new UpstreamTerminalSection(TERMINAL_TO_JAMSIL);

        assertThatThrownBy(() -> givenSection.insertInTheMiddle(DummyTerminalStation.getInstance(), DISTANCE))
                .isInstanceOf(DuplicateStationInLineException.class);
    }

    @Test
    @DisplayName("merge하면 (상행역1)--(하행역1), (상행역1)--(하행역2)에서 (상행역1)--(하행역2)가 반환된다")
    void merge() {
        final UpstreamTerminalSection givenSection = new UpstreamTerminalSection(TERMINAL_TO_JAMSIL);

        final MiddleSection sectionToMerge = new MiddleSection(JAMSIL_TO_JAMSILNARU);

        assertThat(givenSection.merge(sectionToMerge)).isEqualTo(TERMINAL_TO_JAMSIL_NARU);
    }

    @Test
    @DisplayName("sectionToMerge가 MiddleSection이 아닌 경우 예외가 발생한다")
    void mergeFail() {
        final UpstreamTerminalSection givenSection = new UpstreamTerminalSection(TERMINAL_TO_JAMSIL);

        final DownstreamTerminalSection sectionToMerge = new DownstreamTerminalSection(JAMSIL_TO_TERMINAL);

        assertThatThrownBy(() -> givenSection.merge(sectionToMerge))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
