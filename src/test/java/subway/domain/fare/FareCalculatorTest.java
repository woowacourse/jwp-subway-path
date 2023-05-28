package subway.domain.fare;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    FareCalculator fareCalculator;
    @BeforeEach
    void setUp() {
        fareCalculator = new FareCalculator(new DistanceFareStrategy());
    }

    @Nested
    @DisplayName("FareStrategy 에 따라 요금을 계산한다.")
    class calculateFare {

        @DisplayName("10km 이하인 경우 기본 요금을 반환한다.")
        @Test
        void calculateFare_10KM이하() {
            // given
            int distance = 5;

            // when
            int fare = fareCalculator.calculateFare(distance);

            // then
            assertThat(fare).isEqualTo(1250);
        }

        @DisplayName("10km 초과, 50km 이하인 경우 추가 운임을 계산하여 반환한다.")
        @ParameterizedTest
        @CsvSource({
                "15, 1350",
                "16, 1450",
                "25, 1550",
                "50, 2050",
        })
        void calculateFare_10KM초과50KM이하(int distance, int expectedFare) {
            // when
            int fare = fareCalculator.calculateFare(distance);

            // then
            assertThat(fare).isEqualTo(expectedFare);
        }

        @DisplayName("50km 초과인 경우 추가 운임을 계산하여 반환한다.")
        @ParameterizedTest
        @CsvSource({
                "58, 2150",
                "59, 2250",
                "64, 2250"
        })
        void calculateFare_50KM초과(int distance, int expectedFare) {
            // when
            int fare = fareCalculator.calculateFare(distance);

            // then
            assertThat(fare).isEqualTo(expectedFare);
        }
    }
}
