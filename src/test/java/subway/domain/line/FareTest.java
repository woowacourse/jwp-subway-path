package subway.domain.line;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidFareException;

class FareTest {

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    @DisplayName("추가 요금이 0보다 작으면 예외를 던진다.")
    void validate(final int value) {
        Assertions.assertThatThrownBy(() -> new Fare(value))
                .isInstanceOf(InvalidFareException.class)
                .hasMessage("노선 추가 요금은 0원보다 커야 합니다.");
    }
}
