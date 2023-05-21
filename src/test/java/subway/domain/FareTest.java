package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Fare는 ")
class FareTest {

    private static final int BASIC_FARE = 1_250;
    private static final int ADDITIONAL_FARE = 100;

    @Nested
    @DisplayName("거리에 따른 요금을 계산하는 calculateFare 메서드 테스트")
    class CalculateFareOfDistanceTest {

        @DisplayName("10km 이하 거리가 주어지면 기본 요금으로 계산한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 10})
        void basicFareTest(int input) {
            //given
            Fare fare = Fare.from(input);

            //when
            int fareValue = fare.getFare();

            //then
            assertThat(fareValue).isEqualTo(BASIC_FARE);
        }

        @DisplayName("10km 초과 50km 이하 거리가 주어지면 5km 마다 추가요금을 붙여 계산한다.")
        @ParameterizedTest
        @CsvSource(value = {"11,1", "15,1", "16,2", "20,2", "41,7", "45,7", "46,8", "50,8"})
        void additionalFareTest1(int distance, int multiplier) {
            //given
            Fare fare = Fare.from(distance);

            //when
            int fareValue = fare.getFare();

            //then
            assertThat(fareValue).isEqualTo(BASIC_FARE + ADDITIONAL_FARE * multiplier);
        }

        @DisplayName("50km 초과 거리부터는 8km 마다 추가요금을 붙여 계산한다.")
        @ParameterizedTest
        @CsvSource(value = {"51,1", "58,1", "59,2", "66,2"})
        void additionalFareTest2(int distance, int multiplier) {
            //given
            Fare fare = Fare.from(distance);

            //when
            int fareValue = fare.getFare();

            //then
            assertThat(fareValue).isEqualTo(BASIC_FARE + ADDITIONAL_FARE * 8 + ADDITIONAL_FARE * multiplier);
        }
    }

    @Nested
    @DisplayName("연령대에 따른 요금을 계산하는 applyDiscountRateOfAge 메서드 테스트")
    class ApplyDiscountRateOfAgeTestGroup {

        @DisplayName("연령대가 성인이면 요금을 그대로 유지한다.")
        @Test
        void adultTest() {
            //given
            Fare fare = Fare.from(1);

            //when
            int fareValue = fare.applyDiscountRateOfAge(AgeGroup.ADULT).getFare();

            //then
            assertThat(fareValue).isEqualTo(BASIC_FARE);
        }

        @DisplayName("연령대가 청소년이면 요금에서 350원을 빼고 20% 할인한다.")
        @Test
        void teenagerTest() {
            //given
            Fare fare = Fare.from(1);

            //when
            int fareValue = fare.applyDiscountRateOfAge(AgeGroup.TEENAGER).getFare();

            //then
            assertThat(fareValue).isEqualTo((int) ((BASIC_FARE - 350) * 0.8));
        }

        @DisplayName("연령대가 어린이이면 요금에서 350원을 빼고 50% 할인한다.")
        @Test
        void childrenTest() {
            //given
            Fare fare = Fare.from(1);

            //when
            int fareValue = fare.applyDiscountRateOfAge(AgeGroup.CHILDREN).getFare();

            //then
            assertThat(fareValue).isEqualTo((int) ((BASIC_FARE - 350) * 0.5));
        }

        @DisplayName("연령대가 노인이면 요금이 무료다.")
        @Test
        void oldTest() {
            //given
            Fare fare = Fare.from(40);

            //when
            int fareValue = fare.applyDiscountRateOfAge(AgeGroup.OLD).getFare();

            //then
            assertThat(fareValue).isEqualTo(0);
        }

        @DisplayName("연령대가 유아면 요금이 무료다.")
        @Test
        void infantTest() {
            //given
            Fare fare = Fare.from(40);

            //when
            int fareValue = fare.applyDiscountRateOfAge(AgeGroup.INFANT).getFare();

            //then
            assertThat(fareValue).isEqualTo(0);
        }
    }
}
