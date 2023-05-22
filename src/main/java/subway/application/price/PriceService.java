package subway.application.price;

import org.springframework.stereotype.Service;
import subway.domain.path.Path;
import subway.domain.price.Price;

@Service
public class PriceService {
    private final PricePolicy pricePolicy;

    public PriceService(PricePolicy pricePolicy) {
        this.pricePolicy = pricePolicy;
    }

    public Price calculate(Path path) {
        Price price = Price.ZERO;
        return price.plus(pricePolicy.calculate(path));
    }
}
