package subway.domain.price.calculator;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.price.Price;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OverFiftyPriceCalculatorTest {

    @Test
    void 오십키로_이하의_거리가_들어오면_0원을_반환하다() {
        final PriceCalculator priceCalculator = new OverFiftyPriceCalculator();
        final Distance distance = Distance.from(50);

        assertThat(priceCalculator.calculateFare(distance)).isEqualTo(Price.from(0));
    }

    @Test
    void 오십키로가_넘는_거리가_들어오면_요금을_반환하다() {
        final PriceCalculator priceCalculator = new OverFiftyPriceCalculator();
        final Distance distance = Distance.from(130);

        assertThat(priceCalculator.calculateFare(distance)).isEqualTo(Price.from(1000));
    }
}
