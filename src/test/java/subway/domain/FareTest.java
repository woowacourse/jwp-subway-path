package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.path.Fare;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FareTest {

    @DisplayName("요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"10:1250", "11:1350", "15:1350", "16:1450", "50:2050", "51:2150", "58:2150", "59:2250"}, delimiter = ':')
    void calculateFareTest(final int distance, final int fare) {
        assertThat(Fare.calculateFare(distance)).isEqualTo(fare);
    }
}
