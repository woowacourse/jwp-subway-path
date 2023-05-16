package subway.domain.subway;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.subway.billing_policy.Fare;
import subway.exception.InvalidFareException;

class FareTest {

    @ParameterizedTest
    @DisplayName("요금이 0 이하라면 예외를 던진다.")
    @ValueSource(ints = {-1, 0})
    void validateWithNonPositiveValue(int input) {
        //given
        //when
        //then
        assertThatThrownBy(() -> new Fare(input))
                .isInstanceOf(InvalidFareException.class)
                .hasMessage("요금은 0보다 커야합니다.");
    }
}
