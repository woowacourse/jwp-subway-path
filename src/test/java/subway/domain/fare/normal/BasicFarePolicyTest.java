package subway.domain.fare.normal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.route.Distance;

class BasicFarePolicyTest {

    private BasicFarePolicy basicFarePolicy;

    @BeforeEach
    void setUp() {
        basicFarePolicy = new BasicFarePolicy();
    }

    @ParameterizedTest(name = "기본 요금을 반환할 수 있는 거리면 true, 아니면 false를 반환한다.")
    @CsvSource(value = {"9:true", "10:true", "11:false"}, delimiter = ':')
    void isAvailable(final int distance, final boolean expected) {
        // given
        final Distance targetDistance = new Distance(distance);

        // expected
        assertThat(basicFarePolicy.isAvailable(targetDistance))
            .isSameAs(expected);
    }

    @Test
    @DisplayName("기본 요금을 반환한다.")
    void calculateFare() {
        // given
        final Distance targetDistance = new Distance(10);

        // expected
        assertThat(basicFarePolicy.calculateFare(targetDistance).fare())
            .isEqualTo(1250);
    }
}
