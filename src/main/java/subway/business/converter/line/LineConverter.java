package subway.business.converter.line;

import subway.business.domain.Line;
import subway.business.dto.LineDto;
import subway.persistence.entity.LineEntity;
import subway.presentation.dto.response.LineResponse;

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
