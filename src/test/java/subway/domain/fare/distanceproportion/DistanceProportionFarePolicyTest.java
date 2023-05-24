package subway.domain.fare.distanceproportion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.fare.Fare;

@SuppressWarnings("NonAsciiCharacters")
class DistanceProportionFarePolicyTest {

    @Test
    void 비정상적인_구간으로_구간요금정책_생성시_예외_발생() {
        final int lowerBoundDistance = 10;
        final int upperBoundDistance = 6;

        assertThatThrownBy(() -> new DistanceProportionFarePolicy(lowerBoundDistance, upperBoundDistance, 10))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("최소 범위는 최대 범위보다 작아야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 할증_구간_단위가_0보다_작거나_같으면_예외_발생(final int invalidSurchargeDistance) {
        final int lowerBoundDistance = 10;
        final int upperBoundDistance = 50;

        assertThatThrownBy(() ->
                new DistanceProportionFarePolicy(lowerBoundDistance, upperBoundDistance, invalidSurchargeDistance)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("할증 단위 범위는 0보다 커야 합니다.");
    }

    @Test
    void 구간에_따른_추가_금액_계산_테스트() {
        final int lowerBoundDistance = 10;
        final int upperBoundDistance = 50;
        final int surchargeDistanceUnit = 5;
        final DistanceProportionFarePolicy farePolicy
                = new DistanceProportionFarePolicy(lowerBoundDistance, upperBoundDistance, surchargeDistanceUnit);

        final Fare calculate = farePolicy.calculate(15);

        assertThat(calculate.getFare()).isEqualTo(100);
    }
}
