package subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static fixtures.LineFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class LineSurchargeFarePolicyTest {

    @Test
    @DisplayName("노선별 추가 요금을 계산한다.")
    void calculateLineSurchargeFareTest() {
        // given
        FarePolicy farePolicy = new LineSurchargeFarePolicy();
        int expectFare = LINE2_SURCHARGE;

        // when
        int fare = farePolicy.calculateFare(100, Set.of(LINE2, LINE7));

        // then
        assertThat(fare).isEqualTo(expectFare);
    }

}