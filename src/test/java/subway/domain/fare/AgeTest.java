package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidAgeException;

class AgeTest {

    @Test
    @DisplayName("나이 객체가 정상적으로 생성된다.")
    void createAge() {
        Age age = new Age(10);

        assertAll(
                () -> assertThat(age).isNotNull(),
                () -> assertThat(age.getValue()).isEqualTo(10)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("나이가 1보다 작은 경우 예외가 발생한다.")
    void createAgeFail(int age) {
        assertThatThrownBy(() -> new Age(age))
                .isInstanceOf(InvalidAgeException.class)
                .hasMessage("나이는 1살 이상 부터 가능합니다.");
    }
}
