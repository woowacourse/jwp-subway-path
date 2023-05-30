package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayFareTest {

    @DisplayName("10km이하는 기본 운임을 부과한다")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10})
    void 십키로_이하는_기본_요금을_부과한다(int distance) {
        //given
        int expected = 1250;
        SubwayFare fare = SubwayFare.decideFare(distance);
        //when
        Integer charge = fare.calculateCharge(distance);
        //then
        assertThat(charge).isEqualTo(expected);
    }

    @DisplayName("10km ~ 50km는 5km당 100원 추가 요금을 부과한다")
    @ParameterizedTest
    @CsvSource(value = {"11,1350", "20,1450", "40,1850", "50,2050"})
    void 십에서_오십은_5키로당_100원_추가_요금_부과한다(int distance, int expected) {
        //given
        SubwayFare fare = SubwayFare.decideFare(distance);
        //when
        Integer charge = fare.calculateCharge(distance);
        //then
        assertThat(charge).isEqualTo(expected);
    }

    @DisplayName("50km 이상은 8km당 100원 추가 요금을 부과한다")
    @ParameterizedTest
    @CsvSource(value = {"51,2150", "58,2150", "59,2250"})
    void 오십이상은_8키로당_100원_추가_요금_부과한다(int distance, int expected) {
        //given
        SubwayFare fare = SubwayFare.decideFare(distance);
        //when
        Integer charge = fare.calculateCharge(distance);
        //then
        assertThat(charge).isEqualTo(expected);
    }
}
