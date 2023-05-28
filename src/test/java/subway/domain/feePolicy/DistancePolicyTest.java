package subway.domain.feePolicy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.feePolicy.DistancePolicy.OVER_51_KM;
import static subway.domain.feePolicy.DistancePolicy._11_To_50_KM;
import static subway.domain.feePolicy.DistancePolicy.DEFAULT;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.Distance;

public class DistancePolicyTest {

    @Test
    void 거리에_따라_세부_정책이_달라진다() {
        assertAll(
                () -> assertThat(DistancePolicy.from(new Distance(0))).isEqualTo(DEFAULT),
                () -> assertThat(DistancePolicy.from(new Distance(10))).isEqualTo(DEFAULT),
                () -> assertThat(DistancePolicy.from(new Distance(11))).isEqualTo(_11_To_50_KM),
                () -> assertThat(DistancePolicy.from(new Distance(50))).isEqualTo(_11_To_50_KM),
                () -> assertThat(DistancePolicy.from(new Distance(51))).isEqualTo(OVER_51_KM)
        );
    }

    @ParameterizedTest
    @CsvSource({"1, 1250", "10, 1250", "11, 1350", "15, 1350", "16, 1450", "21, 1550", "30, 1650", "33, 1750", "36, 1850", "45, 1950", "46, 2050", "50, 2050", "51, 2150", "58, 2150", "59, 2250", "67, 2350", "74, 2350"})
    void 거리_정보를_받아_요금을_계산한다(final double distanceValue, final int expect) {
        // given
        int result = 1250;
        Distance distance = new Distance(distanceValue);
        DistancePolicy policy = DistancePolicy.from(new Distance(distanceValue));

        // when
        result += policy.calculate(distance);

        // then
        assertThat(result).isEqualTo(expect);
    }
}
