package subway.domain.fare;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @Test
    @DisplayName("요금을 생성한다.")
    void create() {
        assertDoesNotThrow(
                () -> new Fare(10)
        );
    }

    @Test
    @DisplayName("요금이 0 이상의 값이 아니면 예외가 발생한다.")
    void create_fail() {
        assertThatThrownBy(
                () -> new Fare(-1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요금은 0 이상의 값이어야 합니다.");
    }

    @Test
    @DisplayName("요금의 합을 구한다.")
    void sum() {
        // given
        Fare fare = new Fare(10);
        Fare other = new Fare(20);

        // when
        Fare actual = fare.sum(other);
        Fare expected = new Fare(30);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
