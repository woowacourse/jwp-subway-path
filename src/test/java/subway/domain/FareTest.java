package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.IllegalFareException;

class FareTest {

    @Test
    @DisplayName("금액이 0원 미만이라면 생성에 실패한다.")
    void createFare_fail() {
        assertThatThrownBy(() -> Fare.from(BigDecimal.valueOf(-1)))
            .isInstanceOf(IllegalFareException.class);
    }

    @Test
    @DisplayName("금액을 더할 수 있다.")
    void add() {
        // given
        Fare fare = Fare.from(BigDecimal.valueOf(100));
        Fare other = Fare.from(BigDecimal.valueOf(1000));

        // when, then
        assertThat(fare.add(other)).isEqualTo(Fare.from(BigDecimal.valueOf(1100)));
    }

    @Test
    @DisplayName("금액에 다른 값을 곱할 수 있다.")
    void multiply() {
        // given
        Fare fare = Fare.from(BigDecimal.valueOf(100));

        // when, then
        assertThat(fare.multiply(BigDecimal.valueOf(2)))
            .isEqualTo(Fare.from(BigDecimal.valueOf(200)));
    }

    @Test
    @DisplayName("소수점에 상관 없이 동등성을 비교할 수 있다.")
    void testEquals() {
        // given
        Fare fare = Fare.from(BigDecimal.valueOf(100.0));
        Fare other = Fare.from(BigDecimal.valueOf(100));

        // when, then
        assertThat(fare.equals(other)).isTrue();
    }
}