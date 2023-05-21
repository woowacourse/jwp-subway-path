package subway.domain.subway;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.subway.billing_policy.Fare;
import subway.exception.InvalidFareException;

class FareTest {

    @ParameterizedTest
    @DisplayName("요금이 0보다 작다면 예외를 던진다.")
    @ValueSource(ints = {-1, Integer.MIN_VALUE})
    void validateWithNonPositiveValue(int input) {
        //given
        //when
        //then
        assertThatThrownBy(() -> new Fare(input))
                .isInstanceOf(InvalidFareException.class)
                .hasMessage("요금은 0이상의 값이어야합니다.");
    }
}
