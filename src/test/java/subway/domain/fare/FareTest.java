package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @DisplayName("요금은 음수일 수 없다.")
    @Test
    void validate() {
        assertThatThrownBy(() -> new Fare(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요금은 음수일 수 없습니다.");
    }

    @DisplayName("add는 값을 더해 반환한다.")
    @Test
    void add() {
        final Fare fare = new Fare(1);
        final Fare result = fare.add(new Fare(2));
        assertThat(result).isEqualTo(new Fare(3));
    }

    @DisplayName("multiply는 값을 곱해 반환한다.")
    @Test
    void multiply() {
        final Fare fare = new Fare(2);
        final Fare result = fare.multiply(10);
        assertThat(result).isEqualTo(new Fare(20));
    }
}
