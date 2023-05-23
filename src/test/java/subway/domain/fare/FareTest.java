package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidFareException;

class FareTest {

    @Test
    @DisplayName("요금 객체가 정상적으로 생성된다.")
    void createFare() {
        Fare fare = new Fare(1000);

        assertAll(
                () -> assertThat(fare).isNotNull(),
                () -> assertThat(fare.getValue()).isEqualTo(1000)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, -5000})
    @DisplayName("요금이 0보다 작은 경우 예외가 발생한다.")
    void createFareFail(int fare) {
        assertThatThrownBy(() -> new Fare(fare))
                .isInstanceOf(InvalidFareException.class)
                .hasMessage("요금은 음수가 될 수 없습니다.");
    }
}
