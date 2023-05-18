package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.IllegalChargeUnitException;

class ChargeUnitTest {
    @Test
    @DisplayName("값이 음수이면 예외가 발생한다.")
    void throwExceptionWhenValueIsNegative() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() -> new ChargeUnit(-190))
                .isInstanceOf(IllegalChargeUnitException.class);
    }
}