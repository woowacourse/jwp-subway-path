package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.strategy.DefaultFareStrategy;
import subway.domain.fare.strategy.InitialAdditionalFareStrategy;
import subway.domain.fare.strategy.SecondaryAdditionalFareStrategy;
import subway.domain.section.Distance;

class SubwayFareCalculatorTest {

    private DefaultFareStrategy defaultFareStrategy;
    private InitialAdditionalFareStrategy initialAdditionalFareStrategy;
    private SecondaryAdditionalFareStrategy secondaryAdditionalFareStrategy;
    private SubwayFareCalculator subwayFareCalculator;

    @BeforeEach
    void setUp() {
        defaultFareStrategy = spy(new DefaultFareStrategy());
        initialAdditionalFareStrategy = spy(new InitialAdditionalFareStrategy());
        secondaryAdditionalFareStrategy = spy(new SecondaryAdditionalFareStrategy());

        subwayFareCalculator = new SubwayFareCalculator(
                List.of(defaultFareStrategy, initialAdditionalFareStrategy, secondaryAdditionalFareStrategy)
        );
    }

    @DisplayName("거리 별로 요금을 반환한다.")
    @ParameterizedTest
    @CsvSource({"10,1250", "11,1350", "15,1350", "16,1450", "20,1450", "50,2050", "51,2150", "58,2150", "59,2250", "66,2250", "67,2350"})
    void test(final int distance, final int fare) {
        final SubwayFareCalculator subwayFareCalculator = new SubwayFareCalculator(
                List.of(
                        new DefaultFareStrategy(),
                        new InitialAdditionalFareStrategy(),
                        new SecondaryAdditionalFareStrategy()
                )
        );
        final Fare result = subwayFareCalculator.calculate(new Distance(distance));
        assertThat(result).isEqualTo(new Fare(fare));
    }

    @DisplayName("1-10까지는 DefaultStrategy를 호출한다.")
    @ParameterizedTest
    @CsvSource({"1", "2", "9", "10"})
    void callDefaultStrategy(final int value) {
        subwayFareCalculator.calculate(new Distance(value));

        verify(defaultFareStrategy, times(1)).calculate(any());
        verify(initialAdditionalFareStrategy, times(0)).calculate(any());
        verify(secondaryAdditionalFareStrategy, times(0)).calculate(any());
    }

    @DisplayName("11-50까지는 InitialAdditionalStrategy를 호출한다.")
    @ParameterizedTest
    @CsvSource({"11", "12", "49", "50"})
    void callInitialAdditionalStrategy(final int value) {
        subwayFareCalculator.calculate(new Distance(value));

        verify(defaultFareStrategy, times(0)).calculate(any());
        verify(initialAdditionalFareStrategy, times(1)).calculate(any());
        verify(secondaryAdditionalFareStrategy, times(0)).calculate(any());
    }

    @DisplayName("51부터는 SecondaryAdditionalStrategy를 호출한다.")
    @ParameterizedTest
    @CsvSource({"51", "52", "100", "1000"})
    void callSecondaryAdditionalStrategy(final int value) {
        subwayFareCalculator.calculate(new Distance(value));

        verify(defaultFareStrategy, times(0)).calculate(any());
        verify(initialAdditionalFareStrategy, times(0)).calculate(any());
        verify(secondaryAdditionalFareStrategy, times(1)).calculate(any());
    }
}
