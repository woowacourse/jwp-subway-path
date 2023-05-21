package subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.IllegalStationNameException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StationNameTest {

    @Test
    @DisplayName("역 이름이 10자 이상이면 예외가 발생한다.")
    void validateStationNameLengthTest() {
        // given
        String invalidStationName = "가나다라마바사아자차카타파하역";

        // when, then
        assertThatThrownBy(() -> new StationName(invalidStationName))
                .isInstanceOf(IllegalStationNameException.class)
                .hasMessage("역 이름은 10자 이하여야 합니다.");
    }
}