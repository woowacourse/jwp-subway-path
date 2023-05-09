package subway.service.converter;

import subway.entity.LineEntity;
import subway.service.dto.LineDto;

public class LineConverter {

    public static LineEntity toEntity(LineDto lineDto) {
        return new LineEntity(lineDto.getName(), lineDto.getColor());
    }

}
