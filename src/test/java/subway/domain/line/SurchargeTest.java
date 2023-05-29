package subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.IllegalSurchargeException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SurchargeTest {

    @Test
    @DisplayName("추가 요금이 음수이면 예외가 발생한다.")
    void validateSurchargeTest() {
        // given
        int surcharge = -1;

        // when, then
        assertThatThrownBy(() -> new Surcharge(surcharge))
                .isInstanceOf(IllegalSurchargeException.class)
                .hasMessage("노선별 추가 요금은 음수일 수 없습니다.");
    }

}