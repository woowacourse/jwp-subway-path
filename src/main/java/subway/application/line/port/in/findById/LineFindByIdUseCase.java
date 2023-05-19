package subway.application.line.port.in.findById;

import subway.application.line.port.in.LineResponseDto;

public interface LineFindByIdUseCase {

    LineResponseDto findById(Long id);
}
