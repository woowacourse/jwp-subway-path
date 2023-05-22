package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @Test
    @DisplayName("요금의 합을 구한다.")
    void add() {
        // given
        final Fare fare = new Fare(0);

        // when
        final Fare result = fare.add(new Fare(10));

        // then
        assertThat(result.fare())
            .isSameAs(10);
    }

    @Test
    @DisplayName("요금의 곱을 구한다.")
    void multiply() {
        // given
        final Fare fare = new Fare(2);

        // when
        final Fare result = fare.multiply(new Fare(100));

        // then
        assertThat(result.fare())
            .isEqualTo(200);
    }

    @Test
    @DisplayName("요금의 차를 구한다.")
    void subtract() {
        // given
        final Fare fare = new Fare(7);

        // when
        final Fare result = fare.subtract(new Fare(3));

        // then
        assertThat(result.fare())
            .isSameAs(4);
    }
}
