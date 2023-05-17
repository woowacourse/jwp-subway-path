package subway.domain.farecalculator.policy.discount;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import subway.dto.FareResponse;

class AgeDiscountPolicyTest {
    AgeDiscountPolicy ageDiscountPolicy = new AgeDiscountPolicy();

    @ParameterizedTest(name = "연령별로 할인된 운임료 리스트를 반환한다.")
    @MethodSource("provideFareAndExpect")
    void discount(int fare, List<Integer> expect) {
        //when
        final List<FareResponse> result = ageDiscountPolicy.discount(fare);

        //then
        final List<Integer> collect = result.stream().map(FareResponse::getFare)
                .collect(Collectors.toUnmodifiableList());
        assertThat(collect).containsExactlyElementsOf(expect);
    }

    public static Stream<Arguments> provideFareAndExpect() {
        return Stream.of(
                Arguments.of(1350, List.of(1350, 800, 500)),
                Arguments.of(2050, List.of(2050, 1360, 850)),
                Arguments.of(1550, List.of(1550, 960, 600))
        );
    }
}
