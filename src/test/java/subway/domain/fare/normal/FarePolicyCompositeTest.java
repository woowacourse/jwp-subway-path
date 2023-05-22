package subway.domain.fare.normal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.Fare;
import subway.domain.route.Distance;

class FarePolicyCompositeTest {

    private FarePolicyComposite farePolicyComposite;

    @BeforeEach
    void setUp() {
        farePolicyComposite = new FarePolicyComposite(
            List.of(new BasicFarePolicy(), new UnitFiveFarePolicy(), new UnitEightFarePolicy()));
    }

    @ParameterizedTest(name = "주어진 거리를 가기 위한 총 금액을 구한다.")
    @CsvSource(value = {"1:1250", "10:1250", "15:1350", "50:2050", "58:2150"}, delimiter = ':')
    void getTotalFare(final int distance, final int fare) {
        // given
        final Distance targetDistance = new Distance(distance);

        // when
        final Fare totalFare = farePolicyComposite.getTotalFare(targetDistance);

        // then
        assertThat(totalFare.fare())
            .isEqualTo(fare);
    }
}
