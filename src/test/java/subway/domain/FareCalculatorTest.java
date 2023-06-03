package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class FareCalculatorTest {

//    @ParameterizedTest(name = "이용거리가 {0}km 일때, 운임 요금은 {1}원이다")
//    @CsvSource(value = {
//            "10, 1250",
//            "12, 1350",
//            "16, 1450",
//            "50, 2050",
//            "58, 2150",
//    })
//    void 운임_요금을_계산한다(final Long distance, final int expectedFare) {
//        // when
//        final int fare = FareCalculator.calculate(distance);
//
//        // then
//        assertThat(fare).isEqualTo(expectedFare);
//    }
}