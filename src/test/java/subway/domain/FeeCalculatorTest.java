package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.TestData._2호선;
import static subway.TestData._4호선;
import static subway.TestData._7호선;
import static subway.TestData.신림선;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import subway.exception.LineNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class FeeCalculatorTest {

    private final FeeCalculator feeCalculator = new FeeCalculator();

    @ParameterizedTest
    @CsvSource({"1, 1250", "10, 1250", "11, 1350", "15, 1350", "16, 1450", "21, 1550", "30, 1650", "33, 1750", "36, 1850", "45, 1950", "46, 2050", "50, 2050", "51, 2150", "58, 2150", "59, 2250", "67, 2350", "74, 2350"})
    void 거리_정보를_받아_요금을_계산한다(final double distance, final int expect) {
        // when
        var result = feeCalculator.calculate(distance);

        // then
        assertThat(result).isEqualTo(expect);
    }

    @ParameterizedTest
    @MethodSource("fee_passLines_expect")
    void 계산된_요금과_통과한_호선_정보로_추가_요금을_계산한다(final int fee, final List<Line> passLines, final int expect) {
        // then
        int result = feeCalculator.addExtraFeeByLine(fee, passLines);

        // expect
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> fee_passLines_expect() {
        return Stream.of(
                Arguments.arguments(1250, List.of(_2호선), 1250),
                Arguments.arguments(1250, List.of(_2호선, _4호선), 1550),
                Arguments.arguments(1250, List.of(_2호선, _7호선), 1650),
                Arguments.arguments(1250, List.of(_2호선, 신림선), 1350),
                Arguments.arguments(1250, List.of(_2호선, _4호선, _7호선, 신림선), 1650)
        );
    }

    @Test
    void 계산된_요금과_통과한_호선_정보로_추가_요금을_계산할_때_호선이_존재하지_않으면_예외가_발생한다() {
        // given
        int fee = 1250;
        List<Line> passLiens = new ArrayList<>();

        // then
        assertThatThrownBy(() -> feeCalculator.addExtraFeeByLine(fee, passLiens))
                .isInstanceOf(LineNotFoundException.class);
    }

    @ParameterizedTest
    @MethodSource("fee_age_expect")
    void 요금과_나이_정보로_할인된_금액을_반환한다(final int fee, final int age, final int expect) {
        // when
        int result = feeCalculator.discountByAge(fee, age);

        // then
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> fee_age_expect() {
        return Stream.of(
                Arguments.arguments(1250, 3, 1250),
                Arguments.arguments(1250, 6, 450),
                Arguments.arguments(1250, 12, 450),
                Arguments.arguments(1250, 13, 720),
                Arguments.arguments(1250, 18, 720),
                Arguments.arguments(1250, 26, 1250)
        );
    }
}
