package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidDistanceException;

class DistanceTest {

    @Test
    @DisplayName("거리를 정상적으로 생성한다.")
    void distance() {
        assertDoesNotThrow(() -> new Distance(10));
    }

    @ParameterizedTest
    @DisplayName("거리가 0보다 작을 경우 예외를 던진다.")
    @ValueSource(ints = {-1, -3})
    void validateWithInvalidRange(final int input) {
        assertThatThrownBy(() -> new Distance(input))
                .isInstanceOf(InvalidDistanceException.class)
                .hasMessage("역 사이의 거리는 0이상이어야합니다.");
    }
}


