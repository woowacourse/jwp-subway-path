package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import subway.exception.IllegalInputForDomainException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class StationTest {

    @DisplayName("생성한다.")
    @Test
    void 생성한다() {
        assertDoesNotThrow(() -> new Station("luca"));
    }

    @DisplayName("이름이 공백이거나 null일 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void 이륾이_공백이거나_null일_경우_예외를_발생한다(final String name) {
        assertThatThrownBy(() -> new Station(name))
                .isInstanceOf(IllegalInputForDomainException.class);
    }

    @DisplayName("10글자 이상의 이름을 받으면 예외를 반환한다")
    @Test
    void 열글자_이상의_이름을_받으면_예외를_반환한다() {
        assertThatThrownBy(() -> new Station("12345678901")).isInstanceOf(IllegalInputForDomainException.class);
    }
}
