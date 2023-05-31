package subway.station.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.station.exception.NameLengthException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class StationTest {

    @ParameterizedTest
    @ValueSource(strings = {"가", "서울대입구서울대16자이름입니다"})
    void 이름이_2이상_15이하가_아니면_예외를_던진다(String invalidStationName) {
        assertThatThrownBy(() -> new Station(invalidStationName))
                .isInstanceOf(NameLengthException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"잠실", "서울대입구서울15자이름입니다"})
    void 이름이_2이상_15이하인_경우_정상_생성된다(String validStationName) {
        assertThatNoException().isThrownBy(() -> new Station(validStationName));
    }
}
