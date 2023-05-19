package subway.domain.farecalculator.policy.discount;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import subway.dto.FareResponse;

class AgeDiscountPolicyTest {
    AgeDiscountPolicy ageDiscountPolicy = new AgeDiscountPolicy();

    @ParameterizedTest(name = "연령별로 할인된 운임료 리스트를 반환한다.")
    @MethodSource("provideFareAndExpect")
    void discount(int fare, int age, Integer expect) {
        //when
        final FareResponse result = ageDiscountPolicy.discount(age, fare);

        //then
        assertThat(result.getFare()).isEqualTo(expect);
    }

    public static Stream<Arguments> provideFareAndExpect() {
        return Stream.of(
                Arguments.of(1350, 5, 1350),
                Arguments.of(1350, 6, 500),
                Arguments.of(1350, 13, 800)
        );
    }
}
