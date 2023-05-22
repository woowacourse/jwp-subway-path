package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class FareTest {
    @ParameterizedTest
    @ValueSource(ints = {-1, -4})
    void 요금이_0보다_작은_같은_경우_예외_발생(final int value) {
        assertThatThrownBy(() -> new Fare(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 요금_덧셈_적용_테스트() {
        final Fare fare = new Fare(300);

        final Fare multiply = fare.add(new Fare(700));

        assertThat(multiply).isEqualTo(new Fare(1000));
    }
}
