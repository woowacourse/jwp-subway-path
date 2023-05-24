package subway.domain.discount;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeTest {

    @Test
    @DisplayName("나이가 음수면 예외가 발생해야 한다.")
    void create_negative() {
        // given
        int age = -1;

        // expect
        assertThatThrownBy(() -> new Age(age))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("나이는 음수가 될 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {151, 152})
    @DisplayName("나이가 150살이 초과하면 예외가 발생해야 한다.")
    void create_overThan150(int age) {
        // expect
        assertThatThrownBy(() -> new Age(age))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("나이는 최대 150살까지 가능합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 20, 150})
    @DisplayName("나이가 정상적으로 생성되어야 한다.")
    void create_success(int input) {
        // when
        Age age = new Age(input);

        // then
        assertThat(age.getAge())
                .isEqualTo(input);
    }
}
