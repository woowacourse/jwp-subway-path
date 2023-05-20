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

    @ParameterizedTest(name = "기본 거리를 제외한 나머지 거리에 대해 5km당 100원씩 부과하여 요금을 계산한다.")
    @CsvSource(value = {"11:1350", "15:1350", "16:1450", "20:1450", "25:1550", "30:1650",
        "35:1750", "40:1850", "45:1950", "50:2050"}, delimiter = ':')
    void calculateFare(final int distance, final int fare) {
        // given
        final Distance targetDistance = new Distance(distance);

        // expected
        assertThat(unitFiveFarePolicy.calculateFare(targetDistance).fare())
            .isEqualTo(fare);
    }
}
