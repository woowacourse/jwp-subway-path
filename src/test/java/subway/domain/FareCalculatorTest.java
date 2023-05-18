package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FareCalculatorTest {

    @DisplayName("거리가 10km 이내일 경우 1250을 반환한다")
    @Test
    void FareIs1250WhenLessThanTen() {
        int fare = FareCalculator.calculateFare(9);

        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("거리가 12km일 경우 1350을 반환한다")
    @Test
    void FareIs1350WhenDistanceIs12() {
        int fare = FareCalculator.calculateFare(12);

        assertThat(fare).isEqualTo(1350);
    }

    @DisplayName("거리가 16km일 경우 1450을 반환한다")
    @Test
    void FareIs1450WhenDistanceIs16() {
        int fare = FareCalculator.calculateFare(16);

        assertThat(fare).isEqualTo(1450);
    }

    @DisplayName("거리가 58km일 경우 2150을 반환한다")
    @Test
    void FareIs2150WhenDistanceIs58() {
        int fare = FareCalculator.calculateFare(58);

        assertThat(fare).isEqualTo(2150);
    }

    @DisplayName("0 이하의 거리가 들어올 경우 예외를 던진다")
    @Test
    void throwExceptionWhenDistanceBelowZero() {
        assertThatThrownBy(() -> FareCalculator.calculateFare(0))
                .isInstanceOf(IllegalStateException.class);
    }
}
