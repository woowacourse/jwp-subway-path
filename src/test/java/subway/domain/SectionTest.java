package subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidDistanceException;

import static org.assertj.core.api.Assertions.*;
import static subway.utils.StationFixture.JAMSIL_NARU_STATION;
import static subway.utils.StationFixture.JAMSIL_STATION;

class SectionTest {

    @Test
    void Section은_하행역_상행역에_해당하는_Station을_갖는다() {
        assertThatNoException()
                .isThrownBy(
                        () -> new Section(JAMSIL_STATION, JAMSIL_NARU_STATION, 5)
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 구간의_거리가_양의정수가_아니면_예외를_던진다(int invalidDistance) {
        assertThatThrownBy(() -> new Section(JAMSIL_STATION, JAMSIL_NARU_STATION, invalidDistance))
                .isInstanceOf(InvalidDistanceException.class);
    }

    @Test
    void 구간의_거리가_양의정수가_아니면_예외를_던진다() {
        assertThatNoException().isThrownBy(() -> new Section(JAMSIL_STATION, JAMSIL_NARU_STATION, 1));
    }

    @Test
    void upstream과_downstream이_일치한다면_true를_반환한다() {
        Section newSection = new Section(JAMSIL_STATION, JAMSIL_NARU_STATION, 3);

        assertThat(newSection.isCorrespondingSection(JAMSIL_STATION, JAMSIL_NARU_STATION)).isTrue();
    }

    @Test
    void upstream과_downstream이_순서가_일치하지_않으면_false를_반환한다() {
        Section newSection = new Section(JAMSIL_STATION, JAMSIL_NARU_STATION, 3);

        assertThat(newSection.isCorrespondingSection(JAMSIL_NARU_STATION, JAMSIL_STATION)).isFalse();
    }

    @Test
    void Station을_중간에_추가해서_새로운_Section들을_반환한다() {
        Section section = new Section(JAMSIL_STATION, JAMSIL_NARU_STATION, 5);
        Station newStation = Station.from("건대입구");

        Section expectedFirstSection = new Section(JAMSIL_STATION, newStation, 2);
        Section expectedSecondSection = new Section(newStation, JAMSIL_NARU_STATION, 3);

        assertThat(section.insertInTheMiddle(newStation, 2))
                .hasSize(2)
                .containsSequence(expectedFirstSection, expectedSecondSection);
    }

    @Test
    void 기존_section_거리_이상의_거리를_넣을_수_없다() {
        Section section = new Section(JAMSIL_STATION, JAMSIL_NARU_STATION, 5);
        Station newStation = Station.from("건대입구");

        assertThatThrownBy(() -> section.insertInTheMiddle(newStation, 5))
                .isInstanceOf(InvalidDistanceException.class);
    }
}
