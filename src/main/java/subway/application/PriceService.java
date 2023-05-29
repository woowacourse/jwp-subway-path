package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Distance;
import subway.domain.price.Price;
import subway.domain.price.PriceCalculatorHandler;

@Service
public class PriceService {

    public Price getSubwayFare(final Distance distance) {
        return PriceCalculatorHandler.progress(distance);
    }
}
