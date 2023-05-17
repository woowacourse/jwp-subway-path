package subway.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import subway.exception.InvalidException;

class StationTest {
    @Test
    @DisplayName("생성 테스트")
    void create() {
        // expected
        assertDoesNotThrow(() -> new Station(1L, "잠실역"));
    }

    @ParameterizedTest(name = "{displayName}[{index}] = ''{0}''")
    @EmptySource
    @DisplayName(value = "이름에 공백이 입력되면 예외가 발생한다.")
    void createEmpty(String name) {
        // expected
        Assertions.assertThatThrownBy(() -> new Station(1L, name)).isInstanceOf(InvalidException.class);
    }
}
