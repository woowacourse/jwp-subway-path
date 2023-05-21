package subway.path.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.line.domain.FareCalculator.calculate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FareCalculatorTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 9, 10})
    void 거리가_10KM_이내인_경우(int distance) {
        int calculate = calculate(distance);
        Assertions.assertThat(calculate).isEqualTo(1_250);
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 12, 14, 15})
    void 거리가_11km_15km_사이인_경우(int distance) {
        int calculate = calculate(distance);
        Assertions.assertThat(calculate).isEqualTo(1_350);
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 20})
    void 거리가_16km_20km_사이인_경우(int distance) {
        int calculate = calculate(distance);
        Assertions.assertThat(calculate).isEqualTo(1_450);
    }

    @ParameterizedTest
    @ValueSource(ints = {51, 58})
    void 거리가_51km_58km_사이인_경우(int distance) {
        int calculate = calculate(distance);
        Assertions.assertThat(calculate).isEqualTo(2_150);
    }

    @ParameterizedTest
    @ValueSource(ints = {59, 66})
    void 거리가_58km_66km_사이인_경우(int distance) {
        int calculate = calculate(distance);
        Assertions.assertThat(calculate).isEqualTo(2_250);
    }

    @Test
    @DisplayName("음수 값이 들어올경우 예외 발생")
    void 음수_값이_들어올경우_예외_발생() {
        assertThatThrownBy(() -> calculate(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리 값은 음수일 수 없습니다. 유효한 거리 값을 입력해주세요.");
    }
}
