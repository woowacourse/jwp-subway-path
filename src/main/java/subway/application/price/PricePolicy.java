package subway.application.price;

import subway.domain.path.Path;
import subway.domain.price.Price;

public interface PricePolicy {
    Price calculate(Path path);
}
