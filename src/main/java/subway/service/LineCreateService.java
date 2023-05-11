package subway.service;

import subway.domain.Line;
import subway.dto.LineCreateDto;

public interface LineCreateService {

    Line createLine(final LineCreateDto lineCreateDto);
}
