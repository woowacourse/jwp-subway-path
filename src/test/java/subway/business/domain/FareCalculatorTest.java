package subway.business.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import subway.business.domain.fare.FareCalculator;
import subway.config.FareCalculatorConfig;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = FareCalculatorConfig.class)
public class FareCalculatorTest {

    @Autowired
    private FareCalculator fareCalculator;

    @DisplayName("거리가 10km 이하일 때 기본 요금(1250)을 반환한다.")
    @ParameterizedTest(name = "거리가 {0}이면 요금 {1}를 반환한다.")
    @CsvSource(value = {"10, 1250", "5, 1250", "1, 1250"})
    void shouldCalculateFareWhenInputLessThan10Km(int distance, int expectedFare) {
        assertThat(fareCalculator.calculateByDistance(distance)).isEqualTo(expectedFare);
    }

    @DisplayName("거리가 10km 초과 50km이하일 때 요금을 반환한다.")
    @ParameterizedTest(name = "거리가 {0}이면 요금 {1}를 반환한다.")
    @CsvSource(value = {"11, 1350", "18, 1450", "20, 1450", "50, 2050"})
    void shouldCalculateFareWhenInputOver10KmAndLessThan50Km(int distance, int expectedFare) {
        assertThat(fareCalculator.calculateByDistance(distance)).isEqualTo(expectedFare);
    }

    @DisplayName("거리가 50km 초과일 때 요금을 반환한다.")
    @ParameterizedTest(name = "거리가 {0}이면 요금 {1}를 반환한다.")
    @CsvSource(value = {"51, 2150", "58, 2150", "59, 2250"})
    void shouldCalculateFareWhenInputOver50Km(int distance, int expectedFare) {
        assertThat(fareCalculator.calculateByDistance(distance)).isEqualTo(expectedFare);
    }
}
