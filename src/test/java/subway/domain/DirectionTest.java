package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.Direction.UP;
import static subway.domain.Direction.of;

class DirectionTest {

    @DisplayName("대소문자에 상관없이 enum 객체를 생성한다.")
    @Test
    void createIgnoringCaseTest() {
        final String up = "up";

        final Direction direction = of(up);

        assertThat(direction).isSameAs(UP);
    }

    @DisplayName("잘못된 인자가 들어오면 예외를 던진다.")
    @Test
    void createExceptionTest() {
        final String mal = "exception";

        assertThatThrownBy(() -> of(mal))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
