package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class StationTest {

    @DisplayName("생성한다.")
    @Test
    void create() {
        assertDoesNotThrow(() -> new Station("luca"));
    }

    @DisplayName("이름이 공백이거나 null일 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throwExceptionWhenNameIsNullOrEmpty(final String name) {
        assertThatThrownBy(() -> new Station(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("10글자 이상의 이름을 받으면 예외를 반환한다")
    @Test
    void throwExceptionWhenNameLengthOver10() {
        assertThatThrownBy(() -> new Station("12345678901")).isInstanceOf(IllegalArgumentException.class);
    }
}
