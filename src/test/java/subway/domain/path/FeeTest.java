package subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.fee.InvalidFeeValue;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class FeeTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    @DisplayName("요금이 음수이면 에러를 발생한다.")
    void fee_is_not_below_zero(int distance) {
        // when + then
        assertThatThrownBy(() -> new Fee(distance))
                .isInstanceOf(InvalidFeeValue.class);

    }
}
