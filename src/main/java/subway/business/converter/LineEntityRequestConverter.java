package subway.business.converter;

import subway.persistence.entity.LineEntity;
import subway.presentation.dto.request.LineRequest;

public class LineEntityRequestConverter {

    public static LineEntity toEntity(final LineRequest lineRequest) {
        return new LineEntity(lineRequest.getName(), lineRequest.getColor());
    }
}
