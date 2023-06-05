package subway.line.application.port.in.findById;

import subway.line.application.port.in.LineResponseDto;

public interface LineFindByIdUseCase {

    LineResponseDto findById(Long id);
}
