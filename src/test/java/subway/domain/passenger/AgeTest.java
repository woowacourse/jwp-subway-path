package subway.domain.passenger;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidAgeException;

class AgeTest {

    @ParameterizedTest
    @DisplayName("잘못된 나이가 입력되면 예외를 던진다.")
    @ValueSource(ints = {-1, 151})
    void validateWithInvalidAge(int age) {
        assertThatThrownBy(() -> new Age(age))
                .isInstanceOf(InvalidAgeException.class)
                .hasMessage("나이는 0 ~ 150 사이의 값이어야 합니다.");
    }
}
