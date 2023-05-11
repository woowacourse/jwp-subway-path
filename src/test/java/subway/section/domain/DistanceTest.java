package subway.section.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceTest {

    @DisplayName("양의 정수가 아닌 값이 들어오면 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"-1", "0"})
    void distanceTest(final int value) {
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양의 정수만 가능합니다.");
    }
}
