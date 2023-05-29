package subway.domain.price.calculator;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.price.Price;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BasicPriceCalculatorTest {

    @Test
    void 기본_요금만_계산한다() {
        final PriceCalculator priceCalculator = new BasicPriceCalculator();
        final Distance distance = Distance.from(10);

        assertThat(priceCalculator.calculateFare(distance)).isEqualTo(Price.from(1250));
    }
}
