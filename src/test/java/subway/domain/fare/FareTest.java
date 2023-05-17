package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class FareTest {
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -4})
    void 요금이_0보다_작거나_같은_경우_예외_발생(final int value) {
        assertThatThrownBy(() -> new Fare(value))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
