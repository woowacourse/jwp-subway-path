package subway.domain;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.TestData._2호선;
import static subway.TestData._4호선;
import static subway.TestData._7호선;
import static subway.TestData.봉천역;
import static subway.TestData.신림선;
import static subway.TestData.신림역;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.feePolicy.AgePolicy;
import subway.domain.feePolicy.DistancePolicy;
import subway.exception.LineNotFoundException;
import java.util.List;
import java.util.stream.Stream;

class FeeCalculatorTest {

    @ParameterizedTest
    @CsvSource({"1, 1250", "10, 1250", "11, 1350", "15, 1350", "16, 1450", "21, 1550", "30, 1650", "33, 1750", "36, 1850", "45, 1950", "46, 2050", "50, 2050", "51, 2150", "58, 2150", "59, 2250", "67, 2350", "74, 2350"})
    void 거리_정보를_받아_요금을_계산한다(final double distanceValue, final int expect) {
        // given
        Distance distance = new Distance(distanceValue);
        FeeCalculator feeCalculator = new FeeCalculator(DistancePolicy.from(distance), AgePolicy.DEFAULT);
        Path shortestPath = new Path(List.of(신림역, 봉천역), distance, List.of(_2호선));

        // when
        var result = feeCalculator.calculate(shortestPath);

        // then
        assertThat(result).isEqualTo(expect);
    }

    @ParameterizedTest
    @MethodSource("fee_passLines_expect")
    void 계산된_요금과_통과한_호선_정보로_추가_요금을_계산한다(final List<Line> passLines, final int expect) {
        // given
        FeeCalculator feeCalculator = new FeeCalculator(DistancePolicy.DEFAULT, AgePolicy.DEFAULT);
        Path shortestPath = new Path(List.of(신림역, 봉천역), new Distance(3), passLines);

        // then
        int result = feeCalculator.calculate(shortestPath);

        // expect
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> fee_passLines_expect() {
        return Stream.of(
                Arguments.arguments(List.of(_2호선), 1250),
                Arguments.arguments(List.of(_2호선, _4호선), 1550),
                Arguments.arguments(List.of(_2호선, _7호선), 1650),
                Arguments.arguments(List.of(_2호선, 신림선), 1350),
                Arguments.arguments(List.of(_2호선, _4호선, _7호선, 신림선), 1650)
        );
    }

    @Test
    void 계산된_요금과_통과한_호선_정보로_추가_요금을_계산할_때_호선이_존재하지_않으면_예외가_발생한다() {
        // given
        FeeCalculator feeCalculator = new FeeCalculator(DistancePolicy.DEFAULT, AgePolicy.DEFAULT);
        Path shortestPath = new Path(List.of(신림역, 봉천역), new Distance(1), emptyList());

        // then
        assertThatThrownBy(() -> feeCalculator.calculate(shortestPath))
                .isInstanceOf(LineNotFoundException.class);
    }

    @ParameterizedTest
    @CsvSource({"3, 1250", "6, 450", "12, 450", "13, 720", "18, 720", "26, 1250"})
    void 요금과_나이_정보로_할인된_금액을_반환한다(final int age, final int expect) {
        // given
        FeeCalculator feeCalculator = new FeeCalculator(DistancePolicy.DEFAULT, AgePolicy.from(age));
        Path shortestPath = new Path(emptyList(), new Distance(1), List.of(_2호선));

        // when
        int result = feeCalculator.calculate(shortestPath);

        // then
        assertThat(result).isEqualTo(expect);
    }
}
