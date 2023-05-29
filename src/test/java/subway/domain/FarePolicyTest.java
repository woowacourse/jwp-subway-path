package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FarePolicyTest {

    @DisplayName("10km 이내는 운임이 1250원이다.")
    @Test
    void calculate_fare_under_10km() {
        int distance = 9;

        assertThat(FarePolicy.of(distance).calculate(distance)).isEqualTo(1250);
    }

    @DisplayName("10km~50km는 5km 당 100원이다")
    @ParameterizedTest(name = "10km + {0}km = {1}원")
    @CsvSource({"5,1350", "6,1450", "9,1450", "10,1450", "40,2050"})
    void calculate_fare_between_10km_50km(int extraDistance, int fare) {
        int distance = 10 + extraDistance;

        assertThat(FarePolicy.of(distance).calculate(distance)).isEqualTo(fare);
    }

    @DisplayName("50km 초과는 8km 당 100원이다")
    @ParameterizedTest(name = "50km + {0}km = {1}원")
    @CsvSource({"1,2150", "8, 2150", "9,2250", "16, 2250", "24, 2350"})
    void calculate_fare_over_50km(int extraDistance, int fare) {
        int distance = 50 + extraDistance;

        assertThat(FarePolicy.of(distance).calculate(distance)).isEqualTo(fare);
    }
}
