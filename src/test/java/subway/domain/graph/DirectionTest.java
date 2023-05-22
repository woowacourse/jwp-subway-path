package subway.domain.graph;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DirectionTest {

    @DisplayName("true로 UP을 생성한다.")
    @Test
    void createUp() {
        assertThat(Direction.from(true)).isEqualTo(Direction.UP);
    }

    @DisplayName("false로 DOWN을 생성한다.")
    @Test
    void createDown() {
        assertThat(Direction.from(false)).isEqualTo(Direction.DOWN);
    }

    @DisplayName("UP은 getOpposite에서 DOWN을 반환한다.")
    @Test
    void getOppositeOfUp() {
        assertThat(Direction.UP.getOpposite()).isEqualTo(Direction.DOWN);
    }

    @DisplayName("DOWN은 getOpposite에서 UP을 반환한다.")
    @Test
    void getOppositeOfDown() {
        assertThat(Direction.DOWN.getOpposite()).isEqualTo(Direction.UP);
    }
}
