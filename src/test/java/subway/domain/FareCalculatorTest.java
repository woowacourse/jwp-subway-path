package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("요금 계산기 단위 테스트")
class FareCalculatorTest {

    @DisplayName("전달받은 이용 거리에 따른 요금을 계산해 반환한다")
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "12:1350", "16:1450", "58:2150"}, delimiter = ':')
    void calculate(int distance, int fare) {
        assertThat(FareCalculator.calculate(new Distance(distance)))
                .isEqualTo(fare);
    }
}
