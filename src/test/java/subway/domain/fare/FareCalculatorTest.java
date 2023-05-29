package subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static fixtures.LineFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @Test
    @DisplayName("가격 정책들을 모두 포함한 가격을 계산한다.")
    void fareCalculateTest() {
        // given
        FareCalculator fareCalculator = new FareCalculator(List.of(new BasicFarePolicy(), new DistanceFarePolicy(), new LineSurchargeFarePolicy()));
        int basicFare = 1250;
        int distance50Surcharge = 800;
        int expectFare = basicFare + distance50Surcharge + LINE2_SURCHARGE + LINE7_SURCHARGE;

        // when
        int fare = fareCalculator.calculate(50, Set.of(LINE2, LINE7));

        // then
        assertThat(fare).isEqualTo(expectFare);
    }
}