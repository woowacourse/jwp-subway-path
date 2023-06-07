package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {
    @ParameterizedTest
    @DisplayName("노선의 이름이 1자 이상 10자 이하가 아닌 경우 예외가 발생한다.")
    @ValueSource(strings = {"", "0123456789호선"})
    void createStationByInvalidInput(String name) {
        assertThatThrownBy(() -> new Line(name))
                .isInstanceOf(IllegalArgumentException.class);
    }
}