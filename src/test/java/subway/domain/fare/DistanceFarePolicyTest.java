package subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.path.Path;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("거리별 지하철 요금을 계산할 수 있다.")
class DistanceFarePolicyTest {

    @ParameterizedTest(name = "거리가 {0}km일 때 추가 요금은 {1}원이다.")
    @CsvSource(value = {"10:0", "11:100", "15:100", "16:200", "20:200", "50:800",
            "51:900", "58:900", "59:1000", "66:1000", "67:1100"}, delimiter = ':')
    void calculateDistanceFareTest(int distance, int expectSurcharge) {
        // given
        FarePolicy farePolicy = new DistanceFarePolicy();
        Path path = new Path(new ArrayList<>(), new ArrayList<>(), distance);

        // when
        int fare = farePolicy.calculateFare(path);

        // then
        assertThat(fare).isEqualTo(expectSurcharge);
    }
}