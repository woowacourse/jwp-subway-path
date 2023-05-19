package subway.domain.calculator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class FeeCalculatorTest {
    private final static FeeCalculator feeCalculator = new DefaultFee();

    @ParameterizedTest(name = "{0}")
    @MethodSource("distanceProvider")
    void calculateFeeTest(final String name, final double distance, final int expected) {
        Assertions.assertThat(feeCalculator.calculate(distance)).isEqualTo(expected);
    }

    private static Stream<Arguments> distanceProvider() {
        return Stream.of(
                Arguments.of("9km이면 요금은 1250원이다.", 9, 1250),
                Arguments.of("10km이면 요금은 1250원이다.", 10, 1250),
                Arguments.of("14km이면 요금은 1350원이다.", 14, 1350),
                Arguments.of("19km이면 요금은 1450원이다.", 19, 1450),
                Arguments.of("24km이면 요금은 1550원이다.", 24, 1550),
                Arguments.of("44km이면 요금은 1950원이다.", 44, 1950),
                Arguments.of("49km이면 요금은 2050원이다.", 49, 2050),
                Arguments.of("50km이면 요금은 2050원이다.", 50, 2050),
                Arguments.of("57km이면 요금은 2150원이다.", 57, 2150),
                Arguments.of("58km이면 요금은 2150원이다.", 58, 2150)
        );
    }
}
