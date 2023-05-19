package subway.application.line.port.in.create;

import subway.application.line.port.in.LineResponseDto;

public interface LineCreateUseCase {

    LineResponseDto createLine(LineCreateRequestDto lineCreateRequestDto);
}
