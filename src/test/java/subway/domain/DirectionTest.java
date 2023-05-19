package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.service.domain.Direction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DirectionTest {

    @ParameterizedTest
    @DisplayName("Direction.Up 을 제대로 생성한다.")
    @ValueSource(strings = {"Up", "up", "UP"})
    void createUpDirection_success(String input) {
        Direction direction = Direction.from(input);

        assertThat(direction).isEqualTo(Direction.UP);
    }

    @ParameterizedTest
    @DisplayName("Direction.Down 을 제대로 생성한다.")
    @ValueSource(strings = {"Down", "down", "DOWN"})
    void createDownDirection_success(String input) {
        Direction direction = Direction.from(input);

        assertThat(direction).isEqualTo(Direction.DOWN);
    }

    @ParameterizedTest
    @DisplayName("잘못된 방향을 입력한 경우 예외가 발생한다.")
    @ValueSource(strings = {"ditoo", "matthew", "bingbong"})
    void createDirection_fail(String input) {
        assertThatThrownBy(() -> Direction.from(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
