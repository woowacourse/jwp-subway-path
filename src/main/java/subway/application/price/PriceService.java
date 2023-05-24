package subway.application.price;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.path.Path;
import subway.domain.price.Price;

@Service
public class PriceService {
    private final List<PricePolicy> pricePolicies;

    public PriceService(List<PricePolicy> pricePolicies) {
        this.pricePolicies = pricePolicies;
    }

    public Price calculate(Path path) {
        Price price = Price.ZERO;
        for (PricePolicy pricePolicy : pricePolicies) {
            price = price.plus(pricePolicy.calculate(path));
        }
        return price;
    }
}
