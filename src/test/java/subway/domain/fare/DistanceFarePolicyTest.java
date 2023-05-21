package subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("거리별 지하철 요금을 계산할 수 있다.")
class DistanceFarePolicyTest {

    @ParameterizedTest(name = "거리가 {0}km일 때 요금은 {1}원이다.")
    @CsvSource(value = {"10:1250", "11:1350", "15:1350", "16:1450", "20:1450", "50:2050",
            "51:2150", "58:2150", "59:2250", "66:2250", "67:2350"}, delimiter = ':')
    void calculateFareTest(int distance, int expectFare) {
        // given
        FareCalculator fareCalculator = new FareCalculator(new DistanceFarePolicy());

        // when
        int fare = fareCalculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(expectFare);
    }
}