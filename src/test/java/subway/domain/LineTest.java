package subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.DuplicateStationInLineException;
import subway.exception.NameLengthException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.utils.LineFixture.LINE_NUMBER_TWO;
import static subway.utils.SectionFixture.JAMSIL_TO_JAMSILNARU;
import static subway.utils.StationFixture.JAMSIL_STATION;
import static subway.utils.StationFixture.SULLEUNG_STATION;

class LineTest {

    @Test
    void Line에_해당하는_Section들을_갖는다() {
        assertThatNoException()
                .isThrownBy(
                        () -> new Line("2호선", List.of(JAMSIL_TO_JAMSILNARU))
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"가", "서울대입구서울대16자이름입니다"})
    void 이름이_2이상_15이하가_아니면_예외를_던진다(String invalidLineName) {
        assertThatThrownBy(() -> new Line(invalidLineName, List.of(JAMSIL_TO_JAMSILNARU)))
                .isInstanceOf(NameLengthException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"잠실", "서울대입구서울15자이름입니다"})
    void 이름이_2이상_15이하인_경우_정상_생성된다(String validLineName) {
        assertThatNoException().isThrownBy(() -> new Line(validLineName, List.of(JAMSIL_TO_JAMSILNARU)));
    }

    /**
     * LINE_NUMBER_TWO
     * 선릉 --(거리: 5)--> 잠실 --(거리: 5)--> 잠실나루
     */
    @Test
    void 추가하려는_Station이_이미_존재하는_경우_예외를_던진다() {
        assertThatThrownBy(() -> LINE_NUMBER_TWO.addStation(JAMSIL_STATION, Station.getEndpoint(), SULLEUNG_STATION, 2))
                .isInstanceOf(DuplicateStationInLineException.class);
    }
}