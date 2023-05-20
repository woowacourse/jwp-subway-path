package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class StationTest {

    @ParameterizedTest
    @ValueSource(strings = {"역", "강남역", "가가가가가가가가가역"})
    @DisplayName("역 이름이 1자 이상 10자 이하면 정상적으로 역이 생성된다")
    public void createStationTest(String name) {
        assertDoesNotThrow(() -> new Station(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "가가가가가가가가가가가가역"})
    @DisplayName("역 이름이 1자 이하 10자 이상면 예외가 발생한다")
    public void createStationErrorTest(String name) {
        assertThatThrownBy(() -> new Station(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역 이름 1자 이상 10자 이하여야 합니다.");
    }

}
