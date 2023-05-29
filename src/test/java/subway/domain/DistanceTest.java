package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceTest {
    
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, 0})
    @DisplayName("0이하의 숫자가 들어오면 예외가 발생한다")
    void create(int value) {
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역 사이의 거리는 양수여야 합니다");
    }
    
    @Test
    @DisplayName("다른 distance보다 거리 값이 크면 true를 반환한다")
    void isLongerThan() {
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(5);
        Assertions.assertThat(distance1.isLongerThan(distance2))
                .isTrue();
    }
    
    @Test
    @DisplayName("뺄 수 없는 distance는 예외를 발생한다")
    void subtract() {
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(5);
        assertThatThrownBy(() -> distance2.subtract(distance1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("더 큰 거리를 뺄 수 없습니다.");
    }
    
    @Test
    @DisplayName("두 거리를 더해 새로운 거리를 생성한다")
    void plus() {
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(5);
        assertThat(distance1.plus(distance2)).isEqualTo(new Distance(15));
    }
}
