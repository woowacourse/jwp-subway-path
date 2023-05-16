package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("FairPolicy는 ")
class FarePolicyTest {

    private static final int BASIC_FARE = 1_250;
    private static final int ADDITIONAL_FARE = 100;

    @DisplayName("10km 이하 거리가 주어지면 기본 요금으로 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    void calculateBasicFareTest(int input) {
        //given
        FarePolicy farePolicy = new FarePolicy();

        //when
        int fare = farePolicy.calculateFare(input);

        //then
        assertThat(fare).isEqualTo(BASIC_FARE);
    }

    @DisplayName("10km 초과 50km 이하 거리가 주어지면 5km 마다 추가요금을 붙여 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"11,1", "15,1", "16,2", "20,2", "41,7", "45,7", "46,8", "50,8"})
    void additionalFareTest1(int distance, int multiplier) {
        //given
        FarePolicy farePolicy = new FarePolicy();

        //when
        int fare = farePolicy.calculateFare(distance);

        //then
        assertThat(fare).isEqualTo(BASIC_FARE + ADDITIONAL_FARE * multiplier);
    }

    @DisplayName("50km 초과 거리부터는 8km 마다 추가요금을 붙여 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"51,1", "58,1", "59,2", "66,2"})
    void additionalFareTest2(int distance, int multiplier) {
        //given
        FarePolicy farePolicy = new FarePolicy();

        //when
        int fare = farePolicy.calculateFare(distance);

        //then
        assertThat(fare).isEqualTo(BASIC_FARE + ADDITIONAL_FARE * 8 + ADDITIONAL_FARE * multiplier);
    }

}
