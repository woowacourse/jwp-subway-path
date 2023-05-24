package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.station.InvalidStationNameLengthException;

class StationTest {
    @ParameterizedTest(name = "역 이름의 길이는 1글자 이상, 10글자 이하여야 한다.")
    @ValueSource(strings = {"역", "우가역오션역쥬니역역"})
    void createStationSuccessTest(String name) {
        assertDoesNotThrow(() -> new Station(name));
    }

    @ParameterizedTest(name = "역 이름의 길이는 1글자 이상, 10글자 이하여야 한다.")
    @ValueSource(strings = {"", "01234567890"})
    void createStationFailTestByNameLength(String name) {
        assertThatThrownBy(() -> new Station(name))
                .isInstanceOf(InvalidStationNameLengthException.class)
                .hasMessage("역 이름은 1글자 이상, 10글자 이하여야 합니다.");
    }
}
