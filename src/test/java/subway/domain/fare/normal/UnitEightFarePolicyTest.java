package subway.domain.fare.normal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.route.Distance;

class UnitEightFarePolicyTest {

    private UnitEightFarePolicy unitEightFarePolicy;

    @BeforeEach
    void setUp() {
        unitEightFarePolicy = new UnitEightFarePolicy();
    }

    @ParameterizedTest(name = "50km 초과의 범위라면 true, 아니면 false를 반환한다.")
    @CsvSource(value = {"51:true", "50:false"}, delimiter = ':')
    void isAvailable(final int distance, final boolean expected) {
        // given
        final Distance targetDistance = new Distance(distance);

        // expected
        assertThat(unitEightFarePolicy.isAvailable(targetDistance))
            .isSameAs(expected);
    }

    @ParameterizedTest(name = "기본 거리를 제외한 나머지 거리에 대해 8km당 100원씩 부과한 요금을 반환한다.")
    @CsvSource(value = {"51:2150", "58:2150", "66:2250"}, delimiter = ':')
    void calculateFare(final int distance, final int fare) {
        // given
        final Distance targetDistance = new Distance(distance);

        // expected
        assertThat(unitEightFarePolicy.calculateFare(targetDistance).fare())
            .isEqualTo(fare);
    }
}
