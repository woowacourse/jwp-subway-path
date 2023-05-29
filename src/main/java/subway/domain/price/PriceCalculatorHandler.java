package subway.domain.price;

import subway.domain.Distance;
import subway.domain.price.calculator.BasicPriceCalculator;
import subway.domain.price.calculator.OverFiftyPriceCalculator;
import subway.domain.price.calculator.PriceCalculator;
import subway.domain.price.calculator.TenToFiftyPriceCalculator;

import java.util.Arrays;

public enum PriceCalculatorHandler {

    BASIC(new BasicPriceCalculator()),
    TEN_TO_FIFTY(new TenToFiftyPriceCalculator()),
    OVER_FIFTY(new OverFiftyPriceCalculator());

    private final PriceCalculator priceCalculator;

    PriceCalculatorHandler(final PriceCalculator priceCalculator) {
        this.priceCalculator = priceCalculator;
    }

    public static Price progress(final Distance distance) {
        return Arrays.stream(values())
                .map(handler -> handler.priceCalculator)
                .map(calculator -> calculator.calculateFare(distance))
                .reduce(Price.from(0), Price::plus);
    }
}
