package subway.domain.fare.normal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.route.Distance;

class UnitFiveFarePolicyTest {

    private UnitFiveFarePolicy unitFiveFarePolicy;

    @BeforeEach
    void setUp() {
        unitFiveFarePolicy = new UnitFiveFarePolicy();
    }

    @ParameterizedTest(name = "11~50km 범위의 거리라면 true, 아니라면 false를 반환한다.")
    @CsvSource(value = {"11:true", "50:true", "10:false", "51:false"}, delimiter = ':')
    void isAvailable(final int distance, final boolean expected) {
        // given
        final Distance targetDistance = new Distance(distance);

        // expected
        assertThat(unitFiveFarePolicy.isAvailable(targetDistance))
            .isSameAs(expected);
    }

    @Test
    @DisplayName("기본 거리를 제외한 나머지 거리에 대해 5km당 100원씩 부과하여 요금을 계산한다.")
    void calculateFare() {
        // given
        final Distance distance = new Distance(12);

        // expected
        assertThat(unitFiveFarePolicy.calculateFare(distance).fare())
            .isEqualTo(1350);
    }
}
