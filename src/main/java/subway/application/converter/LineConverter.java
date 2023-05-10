package subway.application.converter;

import subway.application.dto.LineDto;
import subway.entity.LineEntity;

public class LineConverter {

    public static LineEntity toEntity(LineDto lineDto) {
        return new LineEntity(lineDto.getName(), lineDto.getColor());
    }

}
