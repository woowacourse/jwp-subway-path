package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import subway.exception.InvalidException;

class StationTest {
    @DisplayName("Station 생성 테스트")
    @Test
    void create() {
        // then
        assertDoesNotThrow(() -> new Station(1L, "잠실역"));
    }

    @DisplayName(value = "이름에 공백이 입력되면 예외가 발생한다.")
    @ParameterizedTest(name = "{displayName}[{index}] = ''{0}''")
    @EmptySource
    void createEmptyName(String name) {
        // then
        assertThatThrownBy(() -> new Station(1L, name))
                .isInstanceOf(InvalidException.class)
                .hasMessage("이름은 공백이 될 수 없습니다.");
    }

    @DisplayName("아이디에 0 이하인 수가 입력되면 예외가 발생한다.")
    @Test
    void createNegativeId() {
        // then
        assertThatThrownBy(() -> new Station(-1L, "찰리역"))
                .isInstanceOf(InvalidException.class)
                .hasMessage("아이디는 양수여야 합니다.");
    }
}
