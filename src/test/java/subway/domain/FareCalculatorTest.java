package subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FareCalculatorTest {

    public static final int DEFAULT_FARE = 1250;

    FareCalculator calculator = new Calculator();

    @ParameterizedTest
    @MethodSource("provideDistanceAndExpectedFares")
    void testCalculateFare(Distance distance, int expectedPrice) {
        //given
        //when
        Fare fare = calculator.calculate(distance);
        //then
        assertThat(fare).isEqualTo(new Fare(expectedPrice));
    }

    private static Stream<Arguments> provideDistanceAndExpectedFares() {
        return Stream.of(
                Arguments.of(new Distance(1), DEFAULT_FARE),
                Arguments.of(new Distance(10), DEFAULT_FARE),
                Arguments.of(new Distance(50), DEFAULT_FARE + (50 - 10) / 5 * 100),
                Arguments.of(new Distance(58), DEFAULT_FARE + (40 / 5 * 100) + ((58 - 50) / 8 * 100))
        );
    }


}