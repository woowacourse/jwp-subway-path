package subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class StationTest {

    @ParameterizedTest
    @ValueSource(strings = {"헤나", "루카", "현구막"})
    void 역을_생성한다(final String value) {
        assertDoesNotThrow(() -> new Station(value));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 역_이름이_null이거나_공백일_경우_예외가_발생한다(final String value) {
        assertThatThrownBy(() -> new Station(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "12345678901"})
    void 역_이름_길이가_1미만_10초과일_경우_예외가_발생한다(final String value) {
        assertThatThrownBy(() -> new Station(value))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
