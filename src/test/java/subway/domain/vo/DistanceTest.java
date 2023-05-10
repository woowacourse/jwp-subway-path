package subway.domain.vo;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceTest {

    @DisplayName("거리에 음수 값을 넣으면 예외가 발생한다")
    @Test
    void DistanceTest() {
        //given
        int value = -1;

        //expect
        assertThatIllegalArgumentException().isThrownBy(
            () -> new Distance(value)
        );
    }
}