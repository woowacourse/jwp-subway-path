package subway.domain.farecalculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BasicFareCalculatorTest {

    FareCalculator fareCalculator = new BasicFareCalculator();

    public static Stream<Arguments> provideDistanceAndFare() {
        return Stream.of(
                Arguments.of(9,1250),
                Arguments.of(16,1450),
                Arguments.of(58,2150)
        );
    }

    @ParameterizedTest(name = "거리별 운임을 계산한다.")
    @MethodSource("provideDistanceAndFare")
    void calculateFare(int distance, int expected) {
        //when
        final int result = fareCalculator.calculateFare(distance);

        //then
        assertThat(result).isEqualTo(expected);
    }
}
