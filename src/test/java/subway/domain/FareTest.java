package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidException;

class FareTest {
    private Fare fare;

    @BeforeEach
    public void setUp() {
        fare = new Fare();
    }

    @DisplayName("0km 이하일 때 오류가 발생한다.")
    @ParameterizedTest(name = "[{index}] distance=''{0}'', expected=error")
    @ValueSource(ints = {-1, 0})
    void calculateFareWithDistanceLessThan0(int distance) {
        // then
        assertThatThrownBy(() -> fare.calculateFare(distance))
                .isInstanceOf(InvalidException.class)
                .hasMessage("거리는 양수여야 합니다.");
    }

    @DisplayName("10km 이내일 때 기본 요금은 1250원이다.")
    @ParameterizedTest(name = "[{index}] distance=''{0}'', expected=1250")
    @ValueSource(ints = {1, 9})
    void calculateFareWithDistanceLessThan10(int distance) {
        // when
        int calculateFare = fare.calculateFare(distance);

        // then
        assertThat(calculateFare).isEqualTo(1250);
    }

    @DisplayName("10km 이상 50km 미만일 때 기본 요금은 1250원에 5km 까지 마다 100원씩 추가된다.")
    @ParameterizedTest
    @CsvSource(value = {"10:1350", "11:1350", "15:1350", "16:1450", "49:2050", "50:2050"}, delimiter = ':')
    void calculateFareWithDistanceLessThan50(int distance, int expected) {
        // when
        int calculateFare = fare.calculateFare(distance);

        // then
        assertThat(calculateFare).isEqualTo(expected);
    }

    @DisplayName("50km 초과일 때 기본 요금은 1250원에 50km까지 5km 마다 100원씩 추가되고 50km 초과부터 8km 마다 100원씩 추가된다..")
    @ParameterizedTest
    @CsvSource(value = {"51:2150", "58:2150", "59:2250", "100:2750"}, delimiter = ':')
    void calculateFareWithDistanceOverThan50(int distance, int expected) {
        // when
        int calculateFare = fare.calculateFare(distance);

        // then
        assertThat(calculateFare).isEqualTo(expected);
    }
}
