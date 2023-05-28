package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class StationTest {

    @DisplayName("Station이 정상적으로 생성된다.")
    @ParameterizedTest
    @ValueSource(strings = {"신분당선", "유효한역이름"})
    void Station(String validName) {
        // when & then
        assertDoesNotThrow(() -> new Station(null, validName));
    }

    @DisplayName("유효하지 않은 이름으로 Station 생성 시, IllegalArgumentException이 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "역이름은15자이하만가능합니다."})
    void Station_NameValidationFail(String invalidName) {
        // when & then
        assertThatThrownBy(() -> new Station(null, invalidName))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
