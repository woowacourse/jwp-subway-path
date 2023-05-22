package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FareCalculatorTest {

    @ParameterizedTest(name = "거리가 {0}km 일때 운행요금은 {1}원이다.")
    @CsvSource(delimiter = ':', value = {"9:1250", "12:1350", "16:1450", "58:2150", "10:1350", "50:2050"})
    void calculate_메서드는_이용거리_초과_시_추가운임_부과(int distance, long fare) {
        //when & then
        Assertions.assertThat(FareCalculator.calculate(distance))
                .isEqualTo(fare);
    }

    @ParameterizedTest(name = "거리가 {0}km 이고, 추가요금이 {1}원, 탑승자의 나이가 {2}라면 운행요금은 {3}원이다.")
    @CsvSource(delimiter = ':', value = {"9:0:10:450", "12:200:10:600", "50:0:14:1360"})
    void calculate_메서드는_(int distance, int surcharge, int age, long fare) {
        //when & then
        Assertions.assertThat(FareCalculator.calculate(distance, surcharge, age))
                .isEqualTo(fare);
    }
}
