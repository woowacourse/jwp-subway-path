package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.*;

class FareTest {

    @DisplayName("{0}km - 10km 이내로 기분 운임이 부과되는 경우")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 9})
    void createByDistanceDefault(int distance) {
        // given
        Fare fare = Fare.createByDistance(distance);

        // then
        assertThat(fare.getFare()).isEqualTo(1_250);
    }

    @DisplayName("{0}km - 10km~50Km 추가 운임이 부과되는 경우")
    @ParameterizedTest
    @ValueSource(ints = {10, 25, 50})
    void createByDistanceBetween10To50(int distance) {
        // given
        Fare fare = Fare.createByDistance(distance);
        int addFare = (int) ((Math.ceil((distance - 11) / 5) + 1) * 100);

        // then
        assertThat(fare.getFare()).isEqualTo(1_250 + addFare);
    }

    @DisplayName("{0}km - 50Km 초과로 추가 운임이 부과되는 경우")
    @ParameterizedTest
    @ValueSource(ints = {51, 60, 100})
    void createByDistanceOver50(int distance) {
        // given
        Fare fare = Fare.createByDistance(distance);
        int addSecondFare = (int) ((Math.ceil((distance - 51) / 8) + 1) * 100);

        // then
        assertThat(fare.getFare()).isEqualTo(2_050 + addSecondFare);
    }

    @DisplayName("{0}km - 운임 부과가 불가능해 예외 발생")
    @ParameterizedTest
    @ValueSource(ints = {-5, 0})
    void createByDistanceException(int distance) {
        // then
        assertThatThrownBy(() -> Fare.createByDistance(distance))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
