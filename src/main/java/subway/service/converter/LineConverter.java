package subway.service.converter;

import subway.service.domain.Line;
import subway.service.dto.LineDto;
import subway.entity.LineEntity;
import subway.controller.dto.response.LineResponse;

public class LineConverter {

    public static LineEntity toEntity(LineDto lineDto) {
        return new LineEntity(lineDto.getName(), lineDto.getColor());
    }

    public static LineResponse domainToResponseDto(final Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }

    public static Line entityToDomain(LineEntity lineEntity) {
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

}
