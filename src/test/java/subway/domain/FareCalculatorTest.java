package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FareCalculatorTest {

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 10})
    @DisplayName("거리가 10km 이내일 때는 기본 요금 1250원이 적용된다.")
    void calculate_lessThan10(double distance) {
        // when
        Fare fare = FareCalculator.calculate(Path.of(Collections.emptyList(), Distance.from(distance)));

        // then
        assertThat(fare).isEqualTo(Fare.from(BigDecimal.valueOf(1250)));
    }

    @ParameterizedTest
    @CsvSource(value = {"10.1:1350", "15:1350", "15.1:1450", "50:2050"}, delimiter = ':')
    @DisplayName("거리가 10km 초과 50km 이하면 5km당 100원이 추가된다.")
    void calculate_lessThan50_over10(double distance, int expectedFare) {
        // when
        Fare fare = FareCalculator.calculate(Path.of(Collections.emptyList(), Distance.from(distance)));

        // then
        assertThat(fare).isEqualTo(Fare.from(BigDecimal.valueOf(expectedFare)));
    }

    @ParameterizedTest
    @CsvSource(value = {"50.1:2150", "58:2150", "58.1:2250"}, delimiter = ':')
    @DisplayName("거리가 50km 초과면 8km당 100원이 추가된다.")
    void calculate_over50(double distance, int expectedFare) {
        // when
        Fare fare = FareCalculator.calculate(Path.of(Collections.emptyList(), Distance.from(distance)));

        // then
        assertThat(fare).isEqualTo(Fare.from(BigDecimal.valueOf(expectedFare)));
    }
}