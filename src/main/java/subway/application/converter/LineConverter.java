package subway.application.converter;

import subway.application.domain.Line;
import subway.application.dto.LineDto;
import subway.entity.LineEntity;
import subway.ui.dto.response.LineResponse;

public class LineConverter {

    public static LineEntity toEntity(LineDto lineDto) {
        return new LineEntity(lineDto.getName(), lineDto.getColor());
    }

    public static LineResponse domainToResponseDto(final Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }
}
