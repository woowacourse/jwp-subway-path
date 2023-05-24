package subway.application.price;

import subway.dao.LineDao;
import subway.dao.entity.LineEntity;
import subway.domain.path.Path;
import subway.domain.price.Price;

public class LineExtraFeePricePolicy implements PricePolicy {
    private final LineDao lineDao;

    public LineExtraFeePricePolicy(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public Price calculate(Path path) {
        long extraFee = path.getPassingLineIds().stream()
                .map(lineDao::findById)
                .mapToLong(lineEntity -> lineEntity.map(LineEntity::getExtraFee)
                        .orElse(0L))
                .max().orElse(0);
        return Price.from(extraFee);
    }
}
