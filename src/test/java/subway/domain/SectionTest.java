package subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidDistanceException;
import subway.exception.SectionMergeException;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.domain.Line.EMPTY_ENDPOINT_STATION;
import static subway.utils.StationFixture.*;

@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void Section은_하행역_상행역에_해당하는_Station을_갖는다() {
        assertThatNoException()
                .isThrownBy(
                        () -> new Section(JAMSIL_STATION, JAMSILNARU_STATION, 5)
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 구간의_거리가_양의정수가_아니면_예외를_던진다(int invalidDistance) {
        assertThatThrownBy(() -> new Section(JAMSIL_STATION, JAMSILNARU_STATION, invalidDistance))
                .isInstanceOf(InvalidDistanceException.class);
    }

    @Test
    void 구간의_거리가_양의정수가_아니면_예외를_던진다() {
        assertThatNoException().isThrownBy(() -> new Section(JAMSIL_STATION, JAMSILNARU_STATION, 1));
    }

    @Test
    void upstream과_downstream이_일치한다면_true를_반환한다() {
        Section newSection = new Section(JAMSIL_STATION, JAMSILNARU_STATION, 3);

        assertThat(newSection.containsSameStations(JAMSIL_STATION, JAMSILNARU_STATION)).isTrue();
    }

    @Test
    void upstream과_downstream이_순서가_일치하지_않으면_false를_반환한다() {
        Section newSection = new Section(JAMSIL_STATION, JAMSILNARU_STATION, 3);

        assertThat(newSection.containsSameStations(JAMSILNARU_STATION, JAMSIL_STATION)).isFalse();
    }

    @Test
    void Station을_중간에_추가해서_새로운_Section들을_반환한다() {
        Section section = new Section(JAMSIL_STATION, JAMSILNARU_STATION, 5);
        Station newStation = new Station("건대입구");

        Section expectedFirstSection = new Section(JAMSIL_STATION, newStation, 2);
        Section expectedSecondSection = new Section(newStation, JAMSILNARU_STATION, 3);

        assertThat(section.insertInTheMiddle(newStation, 2))
                .hasSize(2)
                .containsSequence(expectedFirstSection, expectedSecondSection);
    }

    @Test
    void 기존_section_거리_이상의_거리를_넣을_수_없다() {
        Section section = new Section(JAMSIL_STATION, JAMSILNARU_STATION, 5);
        Station newStation = new Station("건대입구");

        assertThatThrownBy(() -> section.insertInTheMiddle(newStation, 5))
                .isInstanceOf(InvalidDistanceException.class);
    }

    @Test
    void 종점역을_포함하는_section을_병합하면_병합하는_section의_거리는_정수_최대가_된다() {
        Section section1 = new Section(SULLEUNG_STATION, JAMSIL_STATION, 5);
        Section section2 = new Section(JAMSIL_STATION, EMPTY_ENDPOINT_STATION, Integer.MAX_VALUE);

        Section mergedSection = section1.merge(section2);

        assertThat(mergedSection.getDistance()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void Section병합시_하나의_Downstream과_다른_하나의_Upstream이_같지_않은_경우_예외를_던진다() {
        Section section1 = new Section(SULLEUNG_STATION, JAMSIL_STATION, 5);
        Section section2 = new Section(JAMSILNARU_STATION, JAMSIL_STATION, 5);

        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> section1.merge(section2))
                    .isInstanceOf(SectionMergeException.class);
            softly.assertThatThrownBy(() -> section2.merge(section1))
                    .isInstanceOf(SectionMergeException.class);
        });
    }

    @Test
    void Section을_병합할_수_있다() {
        Section section1 = new Section(SULLEUNG_STATION, JAMSIL_STATION, 5);
        Section section2 = new Section(JAMSIL_STATION, JAMSILNARU_STATION, 5);

        Section mergedSection1 = section1.merge(section2);
        Section mergedSection2 = section2.merge(section1);

        assertSoftly(softly -> {
            softly.assertThat(mergedSection1.getDistance()).isEqualTo(10);
            softly.assertThat(mergedSection2.getDistance()).isEqualTo(10);
        });
    }
}
