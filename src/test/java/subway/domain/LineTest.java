package subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.DuplicateStationInLineException;
import subway.exception.NameLengthException;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.domain.Line.EMPTY_ENDPOINT_STATION;
import static subway.utils.LineFixture.LINE_NUMBER_TWO;
import static subway.utils.SectionFixture.JAMSIL_TO_JAMSILNARU;
import static subway.utils.StationFixture.*;

@SuppressWarnings("NonAsciiCharacters")
class LineTest {

    @Test
    void Line에_해당하는_Section들을_갖는다() {
        assertThatNoException()
                .isThrownBy(
                        () -> new Line(new LineName("2호선"), List.of(JAMSIL_TO_JAMSILNARU))
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"가", "서울대입구서울대16자이름입니다"})
    void 이름이_2이상_15이하가_아니면_예외를_던진다(String invalidLineName) {
        assertThatThrownBy(() -> new Line(new LineName(invalidLineName), List.of(JAMSIL_TO_JAMSILNARU)))
                .isInstanceOf(NameLengthException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"잠실", "서울대입구서울15자이름입니다"})
    void 이름이_2이상_15이하인_경우_정상_생성된다(String validLineName) {
        assertThatNoException().isThrownBy(() -> new Line(new LineName(validLineName), List.of(JAMSIL_TO_JAMSILNARU)));
    }

    /**
     * LINE_NUMBER_TWO
     * 선릉 --(거리: 5)--> 잠실 --(거리: 5)--> 잠실나루
     */
    @Test
    void 추가하려는_Station이_이미_존재하는_경우_예외를_던진다() {
        assertThatThrownBy(() -> LINE_NUMBER_TWO.addStation(JAMSIL_STATION, EMPTY_ENDPOINT_STATION, SULLEUNG_STATION, 2))
                .isInstanceOf(DuplicateStationInLineException.class);
    }

    @Test
    void 삭제하려는_Station이_없는_경우_예외를_던진다() {
        assertThatThrownBy(() -> LINE_NUMBER_TWO.deleteStation(new Station("메리")))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    void Station을_추가할_때_Upstream과_Downstream이_Section으로_등록되지_않은_경우_예외를_던진다() {
        Station newStation = new Station("에밀");

        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> LINE_NUMBER_TWO.addStation(newStation, SULLEUNG_STATION, JAMSILNARU_STATION, 3))
                    .isInstanceOf(SectionNotFoundException.class);
            softly.assertThatThrownBy(() -> LINE_NUMBER_TWO.addStation(newStation, JAMSIL_STATION, EMPTY_ENDPOINT_STATION, 3))
                    .isInstanceOf(SectionNotFoundException.class);
        });
    }

    @Test
    void Station을_삭제할_수_있다() {
        Line lineNumberTwo = new Line(LINE_NUMBER_TWO);

        lineNumberTwo.deleteStation(JAMSIL_STATION);

        assertThat(lineNumberTwo.getSectionsWithoutEndPoints())
                .contains(new Section(SULLEUNG_STATION, JAMSILNARU_STATION, 10));
    }

    @Test
    void 하행_종점을_삭제하면_종점의_전_역이_종점이_된다() {
        Section section1 = new Section(SULLEUNG_STATION, JAMSIL_STATION, 5);
        Section section2 = new Section(JAMSIL_STATION, JAMSILNARU_STATION, 5);

        Line line = new Line(new LineName("2호선"), List.of(section1, section2));
        line.deleteStation(JAMSILNARU_STATION);

        assertSoftly(softly -> {
            softly.assertThat(line.getSectionsWithoutEndPoints()).hasSize(1);
            softly.assertThat(line.getSectionsWithoutEndPoints()).doesNotContain(section2);
        });
    }

    @Test
    void 상행_종점을_삭제하면_두_번째_역이_상행_종점이_된다() {
        Section section1 = new Section(SULLEUNG_STATION, JAMSIL_STATION, 5);
        Section section2 = new Section(JAMSIL_STATION, JAMSILNARU_STATION, 5);

        Line line = new Line(new LineName("2호선"), List.of(section1, section2));
        line.deleteStation(SULLEUNG_STATION);

        assertSoftly(softly -> {
            softly.assertThat(line.getSectionsWithoutEndPoints()).hasSize(1);
            softly.assertThat(line.getSectionsWithoutEndPoints()).doesNotContain(section1);
        });
    }

    @Test
    void Station을_추가할_수_있다() {
        Line lineNumberTwo = new Line(LINE_NUMBER_TWO);

        Station newStation = new Station("건대입구");
        lineNumberTwo.addStation(newStation, EMPTY_ENDPOINT_STATION, SULLEUNG_STATION, 2);

        assertThat(lineNumberTwo.getSectionsWithoutEndPoints())
                .contains(new Section(newStation, SULLEUNG_STATION, 2));
    }

    @Test
    void Line_생성시_section이_하나도_없으면_예외를_던진다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Line(new LineName("2호선"), List.of()))
                .withMessageContaining("디버깅: section이 존재하지 않습니다.");
    }
}
