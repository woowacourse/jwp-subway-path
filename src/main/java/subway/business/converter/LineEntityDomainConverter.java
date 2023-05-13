package subway.business.converter;

import subway.business.domain.Line;
import subway.persistence.entity.LineEntity;

public class LineEntityDomainConverter {

    public static LineEntity toEntity(final Line line) {
        return new LineEntity(line.getName(), line.getColor());
    }
}
