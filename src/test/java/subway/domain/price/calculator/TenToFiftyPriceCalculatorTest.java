package subway.domain.price.calculator;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.price.Price;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TenToFiftyPriceCalculatorTest {

    @Test
    void 기본_운임_거리가_들어오면_0원을_반환하다() {
        final PriceCalculator priceCalculator = new TenToFiftyPriceCalculator();
        final Distance distance = Distance.from(9);

        assertThat(priceCalculator.calculateFare(distance)).isEqualTo(Price.from(0));
    }

    @Test
    void 십키로_이상_오십키로_이상의_거리가_들어오면_요금을_반환하다() {
        final PriceCalculator priceCalculator = new TenToFiftyPriceCalculator();
        final Distance distance = Distance.from(25);

        assertThat(priceCalculator.calculateFare(distance)).isEqualTo(Price.from(300));
    }

    @Test
    void 오십키로가_넘는_거리가_들어오면_오십키로로_요금을_반환하다() {
        final PriceCalculator priceCalculator = new TenToFiftyPriceCalculator();
        final Distance distance = Distance.from(100);

        assertThat(priceCalculator.calculateFare(distance)).isEqualTo(Price.from(800));
    }
}
